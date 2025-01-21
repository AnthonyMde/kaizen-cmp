import { expect } from 'chai'
import * as admin from 'firebase-admin'
import { describe } from 'mocha'
import * as sinon from 'sinon'
import * as tsSinon from 'ts-sinon'
import { Collection } from '../src/collection'
import { Challenge, ChallengeStatus, NUMBER_OF_DAYS_FOR_DONE } from '../src/dto/challenge'

const test = require('firebase-functions-test')();

const getChallengeCollectionStub = (challengeDocStub: {}) => {
    return {
        docs: [challengeDocStub],
        get: sinon.stub().resolves({ docs: [challengeDocStub] }),
    };
}

const getUserCollectionStub = (userDocStub: {}) => {
    return {
        docs: [userDocStub],
        get: sinon.stub().resolves({ docs: [userDocStub] }),
    };
}

const getFirestoreStubWithCustomChallenge = (challenge: Challenge) => {
    const challengeDocStub = {
        data: sinon.stub().returns(challenge),
        ref: { update: sinon.stub().resolves(), },
    };
    const challengeCollectionStub = getChallengeCollectionStub(challengeDocStub)

    const userDocStub = {
        data: sinon.stub().returns({}),
        ref: {
            collection: sinon.stub().withArgs(Collection.CHALLENGES).returns(challengeCollectionStub),
            update: sinon.stub().resolves(),
        },
    }
    const userCollectionStub = getUserCollectionStub(userDocStub)
    return {
        collection: sinon.stub().withArgs(Collection.USERS).returns(userCollectionStub),
    } as any;
}

const stubTestSession = (firestoreStub: any) => {
    sinon.stub(admin, 'initializeApp');
    sinon.stub(admin, 'firestore')
        .get(function () {
            return function () {
                return firestoreStub;
            }
        });
}

describe("Update challenge tests", () => {
    let firestoreStub: tsSinon.StubbedInstance<any>

    afterEach(() => {
        sinon.restore();
        test.cleanup();
    });

    it("Should reset challenges' isDoneForToday to false", async () => {
        firestoreStub = getFirestoreStubWithCustomChallenge({
            isDoneForToday: true,
            status: ChallengeStatus[ChallengeStatus.ON_GOING],
            days: 1,
            failureCount: 0,
            maxAuthorizedFailures: 1
        } as Challenge)
        stubTestSession(firestoreStub)
        const { checkUserChallengesCron } = await import('../src/testIndex'); // should be done after mocking admin.initializedApp()

        const wrapped = test.wrap(checkUserChallengesCron);

        await wrapped({});

        const userCollection = await firestoreStub.collection(Collection.USERS).get();
        const userDoc = userCollection.docs[0];
        const challengeCollection = await userDoc.ref.collection(Collection.CHALLENGES).get();
        const challengeDoc = challengeCollection.docs[0];

        expect(challengeDoc.ref.update.calledWith(
            {
                isDoneForToday: false,
                failureCount: 0,
                status: ChallengeStatus[ChallengeStatus.ON_GOING],
                days: 2
            }
        )).to.be.true;
    });

    it("Should increment challenge's failures to 1 if isDoneForToday is false", async () => {
        firestoreStub = getFirestoreStubWithCustomChallenge({
            isDoneForToday: false,
            status: ChallengeStatus[ChallengeStatus.ON_GOING],
            days: 1,
            failureCount: 0,
            maxAuthorizedFailures: 1
        } as Challenge)
        stubTestSession(firestoreStub)
        const { checkUserChallengesCron } = await import('../src/testIndex'); // should be done after mocking admin.initializedApp()
        const wrapped = test.wrap(checkUserChallengesCron);

        await wrapped({});

        const userCollection = await firestoreStub.collection(Collection.USERS).get();
        const userDoc = userCollection.docs[0];
        const challengeCollection = await userDoc.ref.collection(Collection.CHALLENGES).get();
        const challengeDoc = challengeCollection.docs[0];

        expect(challengeDoc.ref.update.calledWith(
            {
                isDoneForToday: false,
                failureCount: 1,
                status: ChallengeStatus[ChallengeStatus.ON_GOING],
                days: 2
            }
        )).to.be.true;
    })

    it("Should UPDATE challenge status to FAILED if maxAuthorizedFailures is exceeded", async () => {
        firestoreStub = getFirestoreStubWithCustomChallenge({
            isDoneForToday: false,
            status: ChallengeStatus[ChallengeStatus.ON_GOING],
            days: 1,
            failureCount: 0,
            maxAuthorizedFailures: 0
        } as Challenge)
        stubTestSession(firestoreStub)
        const { checkUserChallengesCron } = await import('../src/testIndex'); // should be done after mocking admin.initializedApp()
        const wrapped = test.wrap(checkUserChallengesCron);

        await wrapped({});

        const userCollection = await firestoreStub.collection(Collection.USERS).get();
        const userDoc = userCollection.docs[0];
        const challengeCollection = await userDoc.ref.collection(Collection.CHALLENGES).get();
        const challengeDoc = challengeCollection.docs[0];

        expect(challengeDoc.ref.update.calledWith(
            {
                isDoneForToday: false,
                failureCount: 1,
                status: ChallengeStatus[ChallengeStatus.FAILED],
                days: 1
            }
        )).to.be.true;
    })

    it("Should UPDATE challenge status to DONE if days equal to 365", async () => {
        firestoreStub = getFirestoreStubWithCustomChallenge({
            isDoneForToday: true,
            status: ChallengeStatus[ChallengeStatus.ON_GOING],
            days: NUMBER_OF_DAYS_FOR_DONE,
            failureCount: 0,
            maxAuthorizedFailures: 0,
        } as Challenge)
        stubTestSession(firestoreStub)
        const { checkUserChallengesCron } = await import('../src/testIndex'); // should be done after mocking admin.initializedApp()
        const wrapped = test.wrap(checkUserChallengesCron);

        await wrapped({});

        const userCollection = await firestoreStub.collection(Collection.USERS).get();
        const userDoc = userCollection.docs[0];
        const challengeCollection = await userDoc.ref.collection(Collection.CHALLENGES).get();
        const challengeDoc = challengeCollection.docs[0];

        expect(challengeDoc.ref.update.calledWith(
            {
                isDoneForToday: false,
                failureCount: 0,
                status: ChallengeStatus[ChallengeStatus.DONE],
                days: NUMBER_OF_DAYS_FOR_DONE
            }
        )).to.be.true;
    })

    it("Should INCREMENT failure to 1 even if days equal to 365", async () => {
        firestoreStub = getFirestoreStubWithCustomChallenge({
            isDoneForToday: false,
            status: ChallengeStatus[ChallengeStatus.ON_GOING],
            days: NUMBER_OF_DAYS_FOR_DONE,
            failureCount: 0,
            maxAuthorizedFailures: 1
        } as Challenge)
        stubTestSession(firestoreStub)
        const { checkUserChallengesCron } = await import('../src/testIndex'); // should be done after mocking admin.initializedApp()
        const wrapped = test.wrap(checkUserChallengesCron);

        await wrapped({});

        const userCollection = await firestoreStub.collection(Collection.USERS).get();
        const userDoc = userCollection.docs[0];
        const challengeCollection = await userDoc.ref.collection(Collection.CHALLENGES).get();
        const challengeDoc = challengeCollection.docs[0];

        expect(challengeDoc.ref.update.calledWith(
            {
                isDoneForToday: false,
                failureCount: 1,
                status: ChallengeStatus[ChallengeStatus.DONE],
                days: NUMBER_OF_DAYS_FOR_DONE
            }
        )).to.be.true;
    })

    it("Should NOT update challenge if status was PAUSED", async () => {
        firestoreStub = getFirestoreStubWithCustomChallenge({
            isDoneForToday: false,
            status: ChallengeStatus[ChallengeStatus.PAUSED],
            days: 1,
            failureCount: 0,
            maxAuthorizedFailures: 1
        } as Challenge)
        stubTestSession(firestoreStub)
        const { checkUserChallengesCron } = await import('../src/testIndex'); // should be done after mocking admin.initializedApp()
        const wrapped = test.wrap(checkUserChallengesCron);

        await wrapped({});

        const userCollection = await firestoreStub.collection(Collection.USERS).get();
        const userDoc = userCollection.docs[0];
        const challengeCollection = await userDoc.ref.collection(Collection.CHALLENGES).get();
        const challengeDoc = challengeCollection.docs[0];

        sinon.assert.notCalled(challengeDoc.ref.update)
    })

    it("Should NOT update challenge if status was DONE", async () => {
        firestoreStub = getFirestoreStubWithCustomChallenge({
            isDoneForToday: false,
            status: ChallengeStatus[ChallengeStatus.DONE],
            days: 1,
            failureCount: 0,
            maxAuthorizedFailures: 1
        } as Challenge)
        stubTestSession(firestoreStub)
        const { checkUserChallengesCron } = await import('../src/testIndex'); // should be done after mocking admin.initializedApp()
        const wrapped = test.wrap(checkUserChallengesCron);

        await wrapped({});

        const userCollection = await firestoreStub.collection(Collection.USERS).get();
        const userDoc = userCollection.docs[0];
        const challengeCollection = await userDoc.ref.collection(Collection.CHALLENGES).get();
        const challengeDoc = challengeCollection.docs[0];

        sinon.assert.notCalled(challengeDoc.ref.update)
    })

    it("Should NOT update challenge if status was FAILED", async () => {
        firestoreStub = getFirestoreStubWithCustomChallenge({
            isDoneForToday: false,
            status: ChallengeStatus[ChallengeStatus.FAILED],
            days: 1,
            failureCount: 0,
            maxAuthorizedFailures: 1
        } as Challenge)
        stubTestSession(firestoreStub)
        const { checkUserChallengesCron } = await import('../src/testIndex'); // should be done after mocking admin.initializedApp()
        const wrapped = test.wrap(checkUserChallengesCron);

        await wrapped({});

        const userCollection = await firestoreStub.collection(Collection.USERS).get();
        const userDoc = userCollection.docs[0];
        const challengeCollection = await userDoc.ref.collection(Collection.CHALLENGES).get();
        const challengeDoc = challengeCollection.docs[0];

        sinon.assert.notCalled(challengeDoc.ref.update)
    })
});
