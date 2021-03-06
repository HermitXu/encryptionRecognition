/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.spinfosec.thrift.dto;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2018-12-19")
public class TableBinaryContent implements org.apache.thrift.TBase<TableBinaryContent, TableBinaryContent._Fields>, java.io.Serializable, Cloneable, Comparable<TableBinaryContent> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TableBinaryContent");

  private static final org.apache.thrift.protocol.TField BINARY_ROW_FIELD_DESC = new org.apache.thrift.protocol.TField("binaryRow", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField COLUMN_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("columnName", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField PK_COLUMN_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("pkColumnName", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField PK_COLUMN_VALUE_FIELD_DESC = new org.apache.thrift.protocol.TField("pkColumnValue", org.apache.thrift.protocol.TType.STRING, (short)4);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new TableBinaryContentStandardSchemeFactory());
    schemes.put(TupleScheme.class, new TableBinaryContentTupleSchemeFactory());
  }

  public ByteBuffer binaryRow; // required
  public String columnName; // required
  public String pkColumnName; // required
  public String pkColumnValue; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    BINARY_ROW((short)1, "binaryRow"),
    COLUMN_NAME((short)2, "columnName"),
    PK_COLUMN_NAME((short)3, "pkColumnName"),
    PK_COLUMN_VALUE((short)4, "pkColumnValue");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // BINARY_ROW
          return BINARY_ROW;
        case 2: // COLUMN_NAME
          return COLUMN_NAME;
        case 3: // PK_COLUMN_NAME
          return PK_COLUMN_NAME;
        case 4: // PK_COLUMN_VALUE
          return PK_COLUMN_VALUE;
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
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.BINARY_ROW, new org.apache.thrift.meta_data.FieldMetaData("binaryRow", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING        , true)));
    tmpMap.put(_Fields.COLUMN_NAME, new org.apache.thrift.meta_data.FieldMetaData("columnName", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PK_COLUMN_NAME, new org.apache.thrift.meta_data.FieldMetaData("pkColumnName", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PK_COLUMN_VALUE, new org.apache.thrift.meta_data.FieldMetaData("pkColumnValue", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TableBinaryContent.class, metaDataMap);
  }

  public TableBinaryContent() {
  }

  public TableBinaryContent(
    ByteBuffer binaryRow,
    String columnName,
    String pkColumnName,
    String pkColumnValue)
  {
    this();
    this.binaryRow = org.apache.thrift.TBaseHelper.copyBinary(binaryRow);
    this.columnName = columnName;
    this.pkColumnName = pkColumnName;
    this.pkColumnValue = pkColumnValue;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TableBinaryContent(TableBinaryContent other) {
    if (other.isSetBinaryRow()) {
      this.binaryRow = org.apache.thrift.TBaseHelper.copyBinary(other.binaryRow);
    }
    if (other.isSetColumnName()) {
      this.columnName = other.columnName;
    }
    if (other.isSetPkColumnName()) {
      this.pkColumnName = other.pkColumnName;
    }
    if (other.isSetPkColumnValue()) {
      this.pkColumnValue = other.pkColumnValue;
    }
  }

  public TableBinaryContent deepCopy() {
    return new TableBinaryContent(this);
  }

  @Override
  public void clear() {
    this.binaryRow = null;
    this.columnName = null;
    this.pkColumnName = null;
    this.pkColumnValue = null;
  }

  public byte[] getBinaryRow() {
    setBinaryRow(org.apache.thrift.TBaseHelper.rightSize(binaryRow));
    return binaryRow == null ? null : binaryRow.array();
  }

  public ByteBuffer bufferForBinaryRow() {
    return org.apache.thrift.TBaseHelper.copyBinary(binaryRow);
  }

  public TableBinaryContent setBinaryRow(byte[] binaryRow) {
    this.binaryRow = binaryRow == null ? (ByteBuffer)null : ByteBuffer.wrap(Arrays.copyOf(binaryRow, binaryRow.length));
    return this;
  }

  public TableBinaryContent setBinaryRow(ByteBuffer binaryRow) {
    this.binaryRow = org.apache.thrift.TBaseHelper.copyBinary(binaryRow);
    return this;
  }

  public void unsetBinaryRow() {
    this.binaryRow = null;
  }

  /** Returns true if field binaryRow is set (has been assigned a value) and false otherwise */
  public boolean isSetBinaryRow() {
    return this.binaryRow != null;
  }

  public void setBinaryRowIsSet(boolean value) {
    if (!value) {
      this.binaryRow = null;
    }
  }

  public String getColumnName() {
    return this.columnName;
  }

  public TableBinaryContent setColumnName(String columnName) {
    this.columnName = columnName;
    return this;
  }

  public void unsetColumnName() {
    this.columnName = null;
  }

  /** Returns true if field columnName is set (has been assigned a value) and false otherwise */
  public boolean isSetColumnName() {
    return this.columnName != null;
  }

  public void setColumnNameIsSet(boolean value) {
    if (!value) {
      this.columnName = null;
    }
  }

  public String getPkColumnName() {
    return this.pkColumnName;
  }

  public TableBinaryContent setPkColumnName(String pkColumnName) {
    this.pkColumnName = pkColumnName;
    return this;
  }

  public void unsetPkColumnName() {
    this.pkColumnName = null;
  }

  /** Returns true if field pkColumnName is set (has been assigned a value) and false otherwise */
  public boolean isSetPkColumnName() {
    return this.pkColumnName != null;
  }

  public void setPkColumnNameIsSet(boolean value) {
    if (!value) {
      this.pkColumnName = null;
    }
  }

  public String getPkColumnValue() {
    return this.pkColumnValue;
  }

  public TableBinaryContent setPkColumnValue(String pkColumnValue) {
    this.pkColumnValue = pkColumnValue;
    return this;
  }

  public void unsetPkColumnValue() {
    this.pkColumnValue = null;
  }

  /** Returns true if field pkColumnValue is set (has been assigned a value) and false otherwise */
  public boolean isSetPkColumnValue() {
    return this.pkColumnValue != null;
  }

  public void setPkColumnValueIsSet(boolean value) {
    if (!value) {
      this.pkColumnValue = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case BINARY_ROW:
      if (value == null) {
        unsetBinaryRow();
      } else {
        setBinaryRow((ByteBuffer)value);
      }
      break;

    case COLUMN_NAME:
      if (value == null) {
        unsetColumnName();
      } else {
        setColumnName((String)value);
      }
      break;

    case PK_COLUMN_NAME:
      if (value == null) {
        unsetPkColumnName();
      } else {
        setPkColumnName((String)value);
      }
      break;

    case PK_COLUMN_VALUE:
      if (value == null) {
        unsetPkColumnValue();
      } else {
        setPkColumnValue((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case BINARY_ROW:
      return getBinaryRow();

    case COLUMN_NAME:
      return getColumnName();

    case PK_COLUMN_NAME:
      return getPkColumnName();

    case PK_COLUMN_VALUE:
      return getPkColumnValue();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case BINARY_ROW:
      return isSetBinaryRow();
    case COLUMN_NAME:
      return isSetColumnName();
    case PK_COLUMN_NAME:
      return isSetPkColumnName();
    case PK_COLUMN_VALUE:
      return isSetPkColumnValue();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof TableBinaryContent)
      return this.equals((TableBinaryContent)that);
    return false;
  }

  public boolean equals(TableBinaryContent that) {
    if (that == null)
      return false;

    boolean this_present_binaryRow = true && this.isSetBinaryRow();
    boolean that_present_binaryRow = true && that.isSetBinaryRow();
    if (this_present_binaryRow || that_present_binaryRow) {
      if (!(this_present_binaryRow && that_present_binaryRow))
        return false;
      if (!this.binaryRow.equals(that.binaryRow))
        return false;
    }

    boolean this_present_columnName = true && this.isSetColumnName();
    boolean that_present_columnName = true && that.isSetColumnName();
    if (this_present_columnName || that_present_columnName) {
      if (!(this_present_columnName && that_present_columnName))
        return false;
      if (!this.columnName.equals(that.columnName))
        return false;
    }

    boolean this_present_pkColumnName = true && this.isSetPkColumnName();
    boolean that_present_pkColumnName = true && that.isSetPkColumnName();
    if (this_present_pkColumnName || that_present_pkColumnName) {
      if (!(this_present_pkColumnName && that_present_pkColumnName))
        return false;
      if (!this.pkColumnName.equals(that.pkColumnName))
        return false;
    }

    boolean this_present_pkColumnValue = true && this.isSetPkColumnValue();
    boolean that_present_pkColumnValue = true && that.isSetPkColumnValue();
    if (this_present_pkColumnValue || that_present_pkColumnValue) {
      if (!(this_present_pkColumnValue && that_present_pkColumnValue))
        return false;
      if (!this.pkColumnValue.equals(that.pkColumnValue))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_binaryRow = true && (isSetBinaryRow());
    list.add(present_binaryRow);
    if (present_binaryRow)
      list.add(binaryRow);

    boolean present_columnName = true && (isSetColumnName());
    list.add(present_columnName);
    if (present_columnName)
      list.add(columnName);

    boolean present_pkColumnName = true && (isSetPkColumnName());
    list.add(present_pkColumnName);
    if (present_pkColumnName)
      list.add(pkColumnName);

    boolean present_pkColumnValue = true && (isSetPkColumnValue());
    list.add(present_pkColumnValue);
    if (present_pkColumnValue)
      list.add(pkColumnValue);

    return list.hashCode();
  }

  @Override
  public int compareTo(TableBinaryContent other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetBinaryRow()).compareTo(other.isSetBinaryRow());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBinaryRow()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.binaryRow, other.binaryRow);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetColumnName()).compareTo(other.isSetColumnName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetColumnName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.columnName, other.columnName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPkColumnName()).compareTo(other.isSetPkColumnName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPkColumnName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.pkColumnName, other.pkColumnName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPkColumnValue()).compareTo(other.isSetPkColumnValue());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPkColumnValue()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.pkColumnValue, other.pkColumnValue);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("TableBinaryContent(");
    boolean first = true;

    sb.append("binaryRow:");
    if (this.binaryRow == null) {
      sb.append("null");
    } else {
      org.apache.thrift.TBaseHelper.toString(this.binaryRow, sb);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("columnName:");
    if (this.columnName == null) {
      sb.append("null");
    } else {
      sb.append(this.columnName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("pkColumnName:");
    if (this.pkColumnName == null) {
      sb.append("null");
    } else {
      sb.append(this.pkColumnName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("pkColumnValue:");
    if (this.pkColumnValue == null) {
      sb.append("null");
    } else {
      sb.append(this.pkColumnValue);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TableBinaryContentStandardSchemeFactory implements SchemeFactory {
    public TableBinaryContentStandardScheme getScheme() {
      return new TableBinaryContentStandardScheme();
    }
  }

  private static class TableBinaryContentStandardScheme extends StandardScheme<TableBinaryContent> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TableBinaryContent struct) throws TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // BINARY_ROW
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.binaryRow = iprot.readBinary();
              struct.setBinaryRowIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // COLUMN_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.columnName = iprot.readString();
              struct.setColumnNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // PK_COLUMN_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.pkColumnName = iprot.readString();
              struct.setPkColumnNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // PK_COLUMN_VALUE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.pkColumnValue = iprot.readString();
              struct.setPkColumnValueIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, TableBinaryContent struct) throws TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.binaryRow != null) {
        oprot.writeFieldBegin(BINARY_ROW_FIELD_DESC);
        oprot.writeBinary(struct.binaryRow);
        oprot.writeFieldEnd();
      }
      if (struct.columnName != null) {
        oprot.writeFieldBegin(COLUMN_NAME_FIELD_DESC);
        oprot.writeString(struct.columnName);
        oprot.writeFieldEnd();
      }
      if (struct.pkColumnName != null) {
        oprot.writeFieldBegin(PK_COLUMN_NAME_FIELD_DESC);
        oprot.writeString(struct.pkColumnName);
        oprot.writeFieldEnd();
      }
      if (struct.pkColumnValue != null) {
        oprot.writeFieldBegin(PK_COLUMN_VALUE_FIELD_DESC);
        oprot.writeString(struct.pkColumnValue);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TableBinaryContentTupleSchemeFactory implements SchemeFactory {
    public TableBinaryContentTupleScheme getScheme() {
      return new TableBinaryContentTupleScheme();
    }
  }

  private static class TableBinaryContentTupleScheme extends TupleScheme<TableBinaryContent> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TableBinaryContent struct) throws TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetBinaryRow()) {
        optionals.set(0);
      }
      if (struct.isSetColumnName()) {
        optionals.set(1);
      }
      if (struct.isSetPkColumnName()) {
        optionals.set(2);
      }
      if (struct.isSetPkColumnValue()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetBinaryRow()) {
        oprot.writeBinary(struct.binaryRow);
      }
      if (struct.isSetColumnName()) {
        oprot.writeString(struct.columnName);
      }
      if (struct.isSetPkColumnName()) {
        oprot.writeString(struct.pkColumnName);
      }
      if (struct.isSetPkColumnValue()) {
        oprot.writeString(struct.pkColumnValue);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TableBinaryContent struct) throws TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.binaryRow = iprot.readBinary();
        struct.setBinaryRowIsSet(true);
      }
      if (incoming.get(1)) {
        struct.columnName = iprot.readString();
        struct.setColumnNameIsSet(true);
      }
      if (incoming.get(2)) {
        struct.pkColumnName = iprot.readString();
        struct.setPkColumnNameIsSet(true);
      }
      if (incoming.get(3)) {
        struct.pkColumnValue = iprot.readString();
        struct.setPkColumnValueIsSet(true);
      }
    }
  }

}

