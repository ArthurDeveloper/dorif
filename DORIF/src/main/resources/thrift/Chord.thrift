namespace java org.dorif.chord
namespace py DORIF.Chord
namespace php DORIF.Chord
/*
* Chord Finger structure definition
*/
struct Finger{
    1:i64 id,
    2:string ip,
    3:i32 port,
    4:string name
}
/*
* Chord FingerTable structure definition
*/
struct FingerTable{
    1:list<Finger> fingers,
    2:list<Finger> successorList
}
/*
* Chord Node structure definition
*/
struct Node{
    1:i64 id,
    2:i32 numBits,
    3:FingerTable fingerTable,
    4:Finger predecessor,
    5:string ip,
    6:i32 port,
    7:string name
}
//Chord Protocol Exceptions
exception UnableToJoinChordException{
    1:i32 errNum = 0,
    2:string errMsg = "An error occurred and the node was unable to join the Chord."
}
exception UnableToFindSuccessorException{
    1:i32 errNum = 1,
    2:string errMsg = "An error occurred and the node was unable to find the key's successor."
}
exception UnableToFindPredecessorException{
    1:i32 errNum = 2,
    2:string errMsg = "An error occurred and the node was unable to find the key's predecessor."
}
exception UnableToFindClosestPrecedingFingerException{
    1:i32 errNum = 3,
    2:string errMsg = "An error occurred and the node was unable to find the closest preceding finger."
}
exception UnableToTransferKeysException{
    1:i32 errNum = 4,
    2:string errMsg = "An error occurred and the node was unable to transfer it's keys."
}
exception UnableToStabilizeException{
    1:i32 errNum = 5,
    2:string errMsg = "An error occurred while executing stabilization protocol."
}
exception UnableToNotifyException{
    1:i32 errNum = 6,
    2:string errMsg = "The node was unable send a notification to other node."
}
exception UnableToFixFingerException{
    1:i32 errNum = 7,
    2:string errMsg = "An error occurred while refreshing the finger table."
}
exception UnableToSendSelfException{
    1:i32 errNum = 8,
    2:string errMsg = "The node was unable to send its data."
}
exception UnableToSetPredecessorException{
    1:i32 errNum = 9,
    2:string errMsg = "The node was unable to set its predecessor."
}
/*
* Chord Protocol Operations
*/
service Chord{
    void join(1:Node n) throws(1:UnableToJoinChordException ex),
    Node findSuccessor(1:i64 id) throws(1:UnableToFindSuccessorException ex),
    Node findPredecessor(1:i64 id) throws(1:UnableToFindPredecessorException ex),
    Node closestPrecedingFinger(1:i64 id) throws(1:UnableToFindClosestPrecedingFingerException ex),
    void transferKeys(1:Node n) throws(1:UnableToTransferKeysException ex),
    void stabilize() throws(1:UnableToStabilizeException ex),
    void notify(1:Node n) throws(1:UnableToNotifyException ex),
    void fixFingers() throws(1:UnableToFixFingerException ex),
    Node sendSelf() throws(1:UnableToSendSelfException ex),
    void setPredecessor(1:Node l) throws(1:UnableToSetPredecessorException ex)
}