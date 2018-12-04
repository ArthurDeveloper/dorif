#
# Autogenerated by Thrift Compiler (0.11.0)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#
#  options string: py
#

from thrift.Thrift import TType, TMessageType, TFrozenDict, TException, TApplicationException
from thrift.protocol.TProtocol import TProtocolException
from thrift.TRecursive import fix_spec

import sys
import DORIF.Chord.ttypes

from thrift.transport import TTransport
all_structs = []


class Term(object):
    """
    Attributes:
     - term
     - frequency
    """


    def __init__(self, term=None, frequency=None,):
        self.term = term
        self.frequency = frequency

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.STRING:
                    self.term = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.I64:
                    self.frequency = iprot.readI64()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('Term')
        if self.term is not None:
            oprot.writeFieldBegin('term', TType.STRING, 1)
            oprot.writeString(self.term.encode('utf-8') if sys.version_info[0] == 2 else self.term)
            oprot.writeFieldEnd()
        if self.frequency is not None:
            oprot.writeFieldBegin('frequency', TType.I64, 2)
            oprot.writeI64(self.frequency)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class RankedDoc(object):
    """
    Attributes:
     - name
     - id
     - desc
     - score
     - serverId
     - serverName
    """


    def __init__(self, name=None, id=None, desc=None, score=None, serverId=None, serverName=None,):
        self.name = name
        self.id = id
        self.desc = desc
        self.score = score
        self.serverId = serverId
        self.serverName = serverName

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.STRING:
                    self.name = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRING:
                    self.id = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 3:
                if ftype == TType.STRING:
                    self.desc = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 4:
                if ftype == TType.DOUBLE:
                    self.score = iprot.readDouble()
                else:
                    iprot.skip(ftype)
            elif fid == 5:
                if ftype == TType.I64:
                    self.serverId = iprot.readI64()
                else:
                    iprot.skip(ftype)
            elif fid == 6:
                if ftype == TType.STRING:
                    self.serverName = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('RankedDoc')
        if self.name is not None:
            oprot.writeFieldBegin('name', TType.STRING, 1)
            oprot.writeString(self.name.encode('utf-8') if sys.version_info[0] == 2 else self.name)
            oprot.writeFieldEnd()
        if self.id is not None:
            oprot.writeFieldBegin('id', TType.STRING, 2)
            oprot.writeString(self.id.encode('utf-8') if sys.version_info[0] == 2 else self.id)
            oprot.writeFieldEnd()
        if self.desc is not None:
            oprot.writeFieldBegin('desc', TType.STRING, 3)
            oprot.writeString(self.desc.encode('utf-8') if sys.version_info[0] == 2 else self.desc)
            oprot.writeFieldEnd()
        if self.score is not None:
            oprot.writeFieldBegin('score', TType.DOUBLE, 4)
            oprot.writeDouble(self.score)
            oprot.writeFieldEnd()
        if self.serverId is not None:
            oprot.writeFieldBegin('serverId', TType.I64, 5)
            oprot.writeI64(self.serverId)
            oprot.writeFieldEnd()
        if self.serverName is not None:
            oprot.writeFieldBegin('serverName', TType.STRING, 6)
            oprot.writeString(self.serverName.encode('utf-8') if sys.version_info[0] == 2 else self.serverName)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class GlobalTermInfo(object):
    """
    Attributes:
     - id
     - term
     - frequency
     - idf
    """


    def __init__(self, id=None, term=None, frequency=None, idf=None,):
        self.id = id
        self.term = term
        self.frequency = frequency
        self.idf = idf

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.I64:
                    self.id = iprot.readI64()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRING:
                    self.term = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 3:
                if ftype == TType.I64:
                    self.frequency = iprot.readI64()
                else:
                    iprot.skip(ftype)
            elif fid == 4:
                if ftype == TType.DOUBLE:
                    self.idf = iprot.readDouble()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('GlobalTermInfo')
        if self.id is not None:
            oprot.writeFieldBegin('id', TType.I64, 1)
            oprot.writeI64(self.id)
            oprot.writeFieldEnd()
        if self.term is not None:
            oprot.writeFieldBegin('term', TType.STRING, 2)
            oprot.writeString(self.term.encode('utf-8') if sys.version_info[0] == 2 else self.term)
            oprot.writeFieldEnd()
        if self.frequency is not None:
            oprot.writeFieldBegin('frequency', TType.I64, 3)
            oprot.writeI64(self.frequency)
            oprot.writeFieldEnd()
        if self.idf is not None:
            oprot.writeFieldBegin('idf', TType.DOUBLE, 4)
            oprot.writeDouble(self.idf)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class DocTermInfo(object):
    """
    Attributes:
     - id
     - term
     - docName
     - frequency
     - tf
     - tfidf
    """


    def __init__(self, id=None, term=None, docName=None, frequency=None, tf=None, tfidf=None,):
        self.id = id
        self.term = term
        self.docName = docName
        self.frequency = frequency
        self.tf = tf
        self.tfidf = tfidf

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.I64:
                    self.id = iprot.readI64()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRING:
                    self.term = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 3:
                if ftype == TType.STRING:
                    self.docName = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 4:
                if ftype == TType.I64:
                    self.frequency = iprot.readI64()
                else:
                    iprot.skip(ftype)
            elif fid == 5:
                if ftype == TType.DOUBLE:
                    self.tf = iprot.readDouble()
                else:
                    iprot.skip(ftype)
            elif fid == 6:
                if ftype == TType.DOUBLE:
                    self.tfidf = iprot.readDouble()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('DocTermInfo')
        if self.id is not None:
            oprot.writeFieldBegin('id', TType.I64, 1)
            oprot.writeI64(self.id)
            oprot.writeFieldEnd()
        if self.term is not None:
            oprot.writeFieldBegin('term', TType.STRING, 2)
            oprot.writeString(self.term.encode('utf-8') if sys.version_info[0] == 2 else self.term)
            oprot.writeFieldEnd()
        if self.docName is not None:
            oprot.writeFieldBegin('docName', TType.STRING, 3)
            oprot.writeString(self.docName.encode('utf-8') if sys.version_info[0] == 2 else self.docName)
            oprot.writeFieldEnd()
        if self.frequency is not None:
            oprot.writeFieldBegin('frequency', TType.I64, 4)
            oprot.writeI64(self.frequency)
            oprot.writeFieldEnd()
        if self.tf is not None:
            oprot.writeFieldBegin('tf', TType.DOUBLE, 5)
            oprot.writeDouble(self.tf)
            oprot.writeFieldEnd()
        if self.tfidf is not None:
            oprot.writeFieldBegin('tfidf', TType.DOUBLE, 6)
            oprot.writeDouble(self.tfidf)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class DocInfo(object):
    """
    Attributes:
     - id
     - name
     - path
     - norma
     - serverId
     - serverName
    """


    def __init__(self, id=None, name=None, path=None, norma=None, serverId=None, serverName=None,):
        self.id = id
        self.name = name
        self.path = path
        self.norma = norma
        self.serverId = serverId
        self.serverName = serverName

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.STRING:
                    self.id = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRING:
                    self.name = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 3:
                if ftype == TType.STRING:
                    self.path = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 4:
                if ftype == TType.DOUBLE:
                    self.norma = iprot.readDouble()
                else:
                    iprot.skip(ftype)
            elif fid == 5:
                if ftype == TType.I64:
                    self.serverId = iprot.readI64()
                else:
                    iprot.skip(ftype)
            elif fid == 6:
                if ftype == TType.STRING:
                    self.serverName = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('DocInfo')
        if self.id is not None:
            oprot.writeFieldBegin('id', TType.STRING, 1)
            oprot.writeString(self.id.encode('utf-8') if sys.version_info[0] == 2 else self.id)
            oprot.writeFieldEnd()
        if self.name is not None:
            oprot.writeFieldBegin('name', TType.STRING, 2)
            oprot.writeString(self.name.encode('utf-8') if sys.version_info[0] == 2 else self.name)
            oprot.writeFieldEnd()
        if self.path is not None:
            oprot.writeFieldBegin('path', TType.STRING, 3)
            oprot.writeString(self.path.encode('utf-8') if sys.version_info[0] == 2 else self.path)
            oprot.writeFieldEnd()
        if self.norma is not None:
            oprot.writeFieldBegin('norma', TType.DOUBLE, 4)
            oprot.writeDouble(self.norma)
            oprot.writeFieldEnd()
        if self.serverId is not None:
            oprot.writeFieldBegin('serverId', TType.I64, 5)
            oprot.writeI64(self.serverId)
            oprot.writeFieldEnd()
        if self.serverName is not None:
            oprot.writeFieldBegin('serverName', TType.STRING, 6)
            oprot.writeString(self.serverName.encode('utf-8') if sys.version_info[0] == 2 else self.serverName)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class TimeOutException(TException):
    """
    Attributes:
     - errNum
     - errMsg
    """


    def __init__(self, errNum=100, errMsg="DORIFServer took too long to respond.",):
        self.errNum = errNum
        self.errMsg = errMsg

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.I32:
                    self.errNum = iprot.readI32()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRING:
                    self.errMsg = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('TimeOutException')
        if self.errNum is not None:
            oprot.writeFieldBegin('errNum', TType.I32, 1)
            oprot.writeI32(self.errNum)
            oprot.writeFieldEnd()
        if self.errMsg is not None:
            oprot.writeFieldBegin('errMsg', TType.STRING, 2)
            oprot.writeString(self.errMsg.encode('utf-8') if sys.version_info[0] == 2 else self.errMsg)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __str__(self):
        return repr(self)

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class DocumentNotFoundException(TException):
    """
    Attributes:
     - errNum
     - errMsg
    """


    def __init__(self, errNum=101, errMsg="Required document was not found.",):
        self.errNum = errNum
        self.errMsg = errMsg

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.I32:
                    self.errNum = iprot.readI32()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRING:
                    self.errMsg = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('DocumentNotFoundException')
        if self.errNum is not None:
            oprot.writeFieldBegin('errNum', TType.I32, 1)
            oprot.writeI32(self.errNum)
            oprot.writeFieldEnd()
        if self.errMsg is not None:
            oprot.writeFieldBegin('errMsg', TType.STRING, 2)
            oprot.writeString(self.errMsg.encode('utf-8') if sys.version_info[0] == 2 else self.errMsg)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __str__(self):
        return repr(self)

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class UnableToAddTermException(TException):
    """
    Attributes:
     - errNum
     - errMsg
    """


    def __init__(self, errNum=102, errMsg="System failed to include the term.",):
        self.errNum = errNum
        self.errMsg = errMsg

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.I32:
                    self.errNum = iprot.readI32()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRING:
                    self.errMsg = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('UnableToAddTermException')
        if self.errNum is not None:
            oprot.writeFieldBegin('errNum', TType.I32, 1)
            oprot.writeI32(self.errNum)
            oprot.writeFieldEnd()
        if self.errMsg is not None:
            oprot.writeFieldBegin('errMsg', TType.STRING, 2)
            oprot.writeString(self.errMsg.encode('utf-8') if sys.version_info[0] == 2 else self.errMsg)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __str__(self):
        return repr(self)

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)


class TermNotFoundException(TException):
    """
    Attributes:
     - errNum
     - errMsg
    """


    def __init__(self, errNum=103, errMsg="Requested term was not found.",):
        self.errNum = errNum
        self.errMsg = errMsg

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.I32:
                    self.errNum = iprot.readI32()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRING:
                    self.errMsg = iprot.readString().decode('utf-8') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('TermNotFoundException')
        if self.errNum is not None:
            oprot.writeFieldBegin('errNum', TType.I32, 1)
            oprot.writeI32(self.errNum)
            oprot.writeFieldEnd()
        if self.errMsg is not None:
            oprot.writeFieldBegin('errMsg', TType.STRING, 2)
            oprot.writeString(self.errMsg.encode('utf-8') if sys.version_info[0] == 2 else self.errMsg)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __str__(self):
        return repr(self)

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)
all_structs.append(Term)
Term.thrift_spec = (
    None,  # 0
    (1, TType.STRING, 'term', 'UTF8', None, ),  # 1
    (2, TType.I64, 'frequency', None, None, ),  # 2
)
all_structs.append(RankedDoc)
RankedDoc.thrift_spec = (
    None,  # 0
    (1, TType.STRING, 'name', 'UTF8', None, ),  # 1
    (2, TType.STRING, 'id', 'UTF8', None, ),  # 2
    (3, TType.STRING, 'desc', 'UTF8', None, ),  # 3
    (4, TType.DOUBLE, 'score', None, None, ),  # 4
    (5, TType.I64, 'serverId', None, None, ),  # 5
    (6, TType.STRING, 'serverName', 'UTF8', None, ),  # 6
)
all_structs.append(GlobalTermInfo)
GlobalTermInfo.thrift_spec = (
    None,  # 0
    (1, TType.I64, 'id', None, None, ),  # 1
    (2, TType.STRING, 'term', 'UTF8', None, ),  # 2
    (3, TType.I64, 'frequency', None, None, ),  # 3
    (4, TType.DOUBLE, 'idf', None, None, ),  # 4
)
all_structs.append(DocTermInfo)
DocTermInfo.thrift_spec = (
    None,  # 0
    (1, TType.I64, 'id', None, None, ),  # 1
    (2, TType.STRING, 'term', 'UTF8', None, ),  # 2
    (3, TType.STRING, 'docName', 'UTF8', None, ),  # 3
    (4, TType.I64, 'frequency', None, None, ),  # 4
    (5, TType.DOUBLE, 'tf', None, None, ),  # 5
    (6, TType.DOUBLE, 'tfidf', None, None, ),  # 6
)
all_structs.append(DocInfo)
DocInfo.thrift_spec = (
    None,  # 0
    (1, TType.STRING, 'id', 'UTF8', None, ),  # 1
    (2, TType.STRING, 'name', 'UTF8', None, ),  # 2
    (3, TType.STRING, 'path', 'UTF8', None, ),  # 3
    (4, TType.DOUBLE, 'norma', None, None, ),  # 4
    (5, TType.I64, 'serverId', None, None, ),  # 5
    (6, TType.STRING, 'serverName', 'UTF8', None, ),  # 6
)
all_structs.append(TimeOutException)
TimeOutException.thrift_spec = (
    None,  # 0
    (1, TType.I32, 'errNum', None, 100, ),  # 1
    (2, TType.STRING, 'errMsg', 'UTF8', "DORIFServer took too long to respond.", ),  # 2
)
all_structs.append(DocumentNotFoundException)
DocumentNotFoundException.thrift_spec = (
    None,  # 0
    (1, TType.I32, 'errNum', None, 101, ),  # 1
    (2, TType.STRING, 'errMsg', 'UTF8', "Required document was not found.", ),  # 2
)
all_structs.append(UnableToAddTermException)
UnableToAddTermException.thrift_spec = (
    None,  # 0
    (1, TType.I32, 'errNum', None, 102, ),  # 1
    (2, TType.STRING, 'errMsg', 'UTF8', "System failed to include the term.", ),  # 2
)
all_structs.append(TermNotFoundException)
TermNotFoundException.thrift_spec = (
    None,  # 0
    (1, TType.I32, 'errNum', None, 103, ),  # 1
    (2, TType.STRING, 'errMsg', 'UTF8', "Requested term was not found.", ),  # 2
)
fix_spec(all_structs)
del all_structs