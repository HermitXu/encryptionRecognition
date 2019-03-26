namespace java com.spinfosec.thrift.dto
namespace py ThriftInterface.EncryptParse.Struct

struct FileMetadata
{
    1:string fileName,     # 文件名称
    2:string filePath,     # 文件路径
    3:string createTime,   # 创建时间
    4:string modifyTime,   # 修改时间
    5:string accessTime,   # 访问时间
    6:string author,       # 作者
    7:string fileType      # 文件类型，此值不用传递
}

# 表字段信息
struct TableColumn
{
    1:string name,                          // 列名称
    2:i32 type,                             // sql 类型，JDK对应类型值
    3:string typeName,                      // sql 类型名称，特定数据库标准名称
    4:string isAutoIncrement,               // 是否自增（YES/NO）
    5:string isNullable,                    // 是否允许为空（YES/NO）
    6:string classification,                // BINARY/STRING/DATE/NUMBER/BOOL/OTHER
    7:string remark                        // 注释，列名称
}

# 表内容
struct TableContent
{
    1:string tableName,                     // 表名称
    2:string pkColumnName,                  // 主键列名称，实际主键
    3:string userPk,                        // 主键列名称，可能为自选
    4:list<TableColumn> columns,            // 列信息
    5:i64 total,                            // 表记录总行数
    6:list<list<string>> rows             // 行集合
}

struct TableBinaryContent
{
    1:binary binaryRow,           // 二进制列的数据
    2:string columnName,          // 二进制列名
    3:string pkColumnName,        // 主键列名称
    4:string pkColumnValue        // 主键列值
}

struct TransforDataInfo
{
    # 策略id
    1:string jobId,
    # 文件二进制
    2:binary fileContent,
    # 文本内容
    3:string fileText,
    # 文件属性信息
    4:FileMetadata fileMetadata,
    # 数据库数据
    5:TableContent tableContent,
     # 数据库二进制数据
    6:TableBinaryContent tableBinaryContent
}

struct Result
{
    1:string code,    # 状态码
    2:string msg      # 信息
}