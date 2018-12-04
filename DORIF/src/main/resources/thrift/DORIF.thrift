include "Chord.thrift"
namespace java org.dorif.handler
namespace py DORIF
namespace php DORIF

struct Term{
    1:string term,
    2:i64 frequency
}
struct RankedDoc{
    1:string name,
    2:string id,
    3:string desc,
    4:double score,
    5:i64 serverId,
    6:string serverName
}
struct GlobalTermInfo{
    1:i64 id,
    2:string term,
    3:i64 frequency,
    4:double idf
}
struct DocTermInfo{
    1:i64 id,
    2:string term,
    3:string docName,
    4:i64 frequency,
    5:double tf,
    6:double tfidf
}
struct DocInfo{
    1:string id,
    2:string name,
    3:string path,
    4:double norma,
    5:i64 serverId,
    6:string serverName
}
exception TimeOutException{
    1:i32 errNum = 100,
    2:string errMsg = "DORIFServer took too long to respond."
}
exception DocumentNotFoundException{
    1:i32 errNum = 101,
    2:string errMsg = "Required document was not found."
}
exception UnableToAddTermException{
    1:i32 errNum = 102,
    2:string errMsg = "System failed to include the term."
}
exception TermNotFoundException{
    1:i32 errNum = 103,
    2:string errMsg = "Requested term was not found."
}
service DORIFService extends Chord.Chord{
    i8 ping() throws(1:TimeOutException toe),
    list<RankedDoc> query(1:string query) throws(1:TimeOutException toe),
    list<RankedDoc> queryServer(1:string query) throws(1:TimeOutException toe),
    binary getDocument(1:i64 id) throws(1:DocumentNotFoundException dnf),
    void addTermo(1:string text) throws(1:UnableToAddTermException utat),
    Term getTermo(1:string text) throws(1:TermNotFoundException tnf),
    bool containsTermo(1:string text),
    i64 getServerNumDocs(),
    i64 getTotalNumDocs(),
    void startCollecting(),
    void stopCollecting(),
    list<Term> getServerGlobalTerms(),
    list<Term> getTotalGlobalTerms(),
    list<GlobalTermInfo> getServerGlobalTermsInfo(),
    list<DocTermInfo> getServerDocTermsInfo(),
    list<GlobalTermInfo> getTotalGlobalTermsInfo(),
    list<DocTermInfo> getTotalDocTermsInfo(),
    list<DocInfo> getServerDocInfos(),
    list<DocInfo> getTotalDocInfo()
}