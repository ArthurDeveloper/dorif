/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.dorif.handler;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2018-11-03")
public class UnableToAddTermException extends org.apache.thrift.TException implements org.apache.thrift.TBase<UnableToAddTermException, UnableToAddTermException._Fields>, java.io.Serializable, Cloneable, Comparable<UnableToAddTermException> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("UnableToAddTermException");

  private static final org.apache.thrift.protocol.TField ERR_NUM_FIELD_DESC = new org.apache.thrift.protocol.TField("errNum", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField ERR_MSG_FIELD_DESC = new org.apache.thrift.protocol.TField("errMsg", org.apache.thrift.protocol.TType.STRING, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new UnableToAddTermExceptionStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new UnableToAddTermExceptionTupleSchemeFactory();

  public int errNum; // required
  public java.lang.String errMsg; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    ERR_NUM((short)1, "errNum"),
    ERR_MSG((short)2, "errMsg");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // ERR_NUM
          return ERR_NUM;
        case 2: // ERR_MSG
          return ERR_MSG;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __ERRNUM_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.ERR_NUM, new org.apache.thrift.meta_data.FieldMetaData("errNum", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.ERR_MSG, new org.apache.thrift.meta_data.FieldMetaData("errMsg", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(UnableToAddTermException.class, metaDataMap);
  }

  public UnableToAddTermException() {
    this.errNum = 102;

    this.errMsg = "System failed to include the term.";

  }

  public UnableToAddTermException(
    int errNum,
    java.lang.String errMsg)
  {
    this();
    this.errNum = errNum;
    setErrNumIsSet(true);
    this.errMsg = errMsg;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public UnableToAddTermException(UnableToAddTermException other) {
    __isset_bitfield = other.__isset_bitfield;
    this.errNum = other.errNum;
    if (other.isSetErrMsg()) {
      this.errMsg = other.errMsg;
    }
  }

  public UnableToAddTermException deepCopy() {
    return new UnableToAddTermException(this);
  }

  @Override
  public void clear() {
    this.errNum = 102;

    this.errMsg = "System failed to include the term.";

  }

  public int getErrNum() {
    return this.errNum;
  }

  public UnableToAddTermException setErrNum(int errNum) {
    this.errNum = errNum;
    setErrNumIsSet(true);
    return this;
  }

  public void unsetErrNum() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __ERRNUM_ISSET_ID);
  }

  /** Returns true if field errNum is set (has been assigned a value) and false otherwise */
  public boolean isSetErrNum() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __ERRNUM_ISSET_ID);
  }

  public void setErrNumIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __ERRNUM_ISSET_ID, value);
  }

  public java.lang.String getErrMsg() {
    return this.errMsg;
  }

  public UnableToAddTermException setErrMsg(java.lang.String errMsg) {
    this.errMsg = errMsg;
    return this;
  }

  public void unsetErrMsg() {
    this.errMsg = null;
  }

  /** Returns true if field errMsg is set (has been assigned a value) and false otherwise */
  public boolean isSetErrMsg() {
    return this.errMsg != null;
  }

  public void setErrMsgIsSet(boolean value) {
    if (!value) {
      this.errMsg = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case ERR_NUM:
      if (value == null) {
        unsetErrNum();
      } else {
        setErrNum((java.lang.Integer)value);
      }
      break;

    case ERR_MSG:
      if (value == null) {
        unsetErrMsg();
      } else {
        setErrMsg((java.lang.String)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case ERR_NUM:
      return getErrNum();

    case ERR_MSG:
      return getErrMsg();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case ERR_NUM:
      return isSetErrNum();
    case ERR_MSG:
      return isSetErrMsg();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof UnableToAddTermException)
      return this.equals((UnableToAddTermException)that);
    return false;
  }

  public boolean equals(UnableToAddTermException that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_errNum = true;
    boolean that_present_errNum = true;
    if (this_present_errNum || that_present_errNum) {
      if (!(this_present_errNum && that_present_errNum))
        return false;
      if (this.errNum != that.errNum)
        return false;
    }

    boolean this_present_errMsg = true && this.isSetErrMsg();
    boolean that_present_errMsg = true && that.isSetErrMsg();
    if (this_present_errMsg || that_present_errMsg) {
      if (!(this_present_errMsg && that_present_errMsg))
        return false;
      if (!this.errMsg.equals(that.errMsg))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + errNum;

    hashCode = hashCode * 8191 + ((isSetErrMsg()) ? 131071 : 524287);
    if (isSetErrMsg())
      hashCode = hashCode * 8191 + errMsg.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(UnableToAddTermException other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetErrNum()).compareTo(other.isSetErrNum());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetErrNum()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.errNum, other.errNum);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetErrMsg()).compareTo(other.isSetErrMsg());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetErrMsg()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.errMsg, other.errMsg);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("UnableToAddTermException(");
    boolean first = true;

    sb.append("errNum:");
    sb.append(this.errNum);
    first = false;
    if (!first) sb.append(", ");
    sb.append("errMsg:");
    if (this.errMsg == null) {
      sb.append("null");
    } else {
      sb.append(this.errMsg);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class UnableToAddTermExceptionStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public UnableToAddTermExceptionStandardScheme getScheme() {
      return new UnableToAddTermExceptionStandardScheme();
    }
  }

  private static class UnableToAddTermExceptionStandardScheme extends org.apache.thrift.scheme.StandardScheme<UnableToAddTermException> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, UnableToAddTermException struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // ERR_NUM
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.errNum = iprot.readI32();
              struct.setErrNumIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // ERR_MSG
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.errMsg = iprot.readString();
              struct.setErrMsgIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, UnableToAddTermException struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(ERR_NUM_FIELD_DESC);
      oprot.writeI32(struct.errNum);
      oprot.writeFieldEnd();
      if (struct.errMsg != null) {
        oprot.writeFieldBegin(ERR_MSG_FIELD_DESC);
        oprot.writeString(struct.errMsg);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class UnableToAddTermExceptionTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public UnableToAddTermExceptionTupleScheme getScheme() {
      return new UnableToAddTermExceptionTupleScheme();
    }
  }

  private static class UnableToAddTermExceptionTupleScheme extends org.apache.thrift.scheme.TupleScheme<UnableToAddTermException> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, UnableToAddTermException struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetErrNum()) {
        optionals.set(0);
      }
      if (struct.isSetErrMsg()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetErrNum()) {
        oprot.writeI32(struct.errNum);
      }
      if (struct.isSetErrMsg()) {
        oprot.writeString(struct.errMsg);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, UnableToAddTermException struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.errNum = iprot.readI32();
        struct.setErrNumIsSet(true);
      }
      if (incoming.get(1)) {
        struct.errMsg = iprot.readString();
        struct.setErrMsgIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}
