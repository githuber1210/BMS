<template>
 <div>
      <el-input style="width: 200px" placeholder="文件名称" v-model="fileName"></el-input>
      <el-button icon="el-icon-search"  @click="load"></el-button>
      <el-upload  :action="serverIp + '/file/upload'" :show-file-list="false"
                 :on-success="handleFileUploadSuccess" style="display: inline-block;margin-left:10px">
          <el-button icon="el-icon-plus" ></el-button>
       </el-upload>    
        <el-popconfirm
          confirm-button-text='确定'
          cancel-button-text='取消'
          icon="el-icon-info"
          icon-color="red"
          title="確定刪除?"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference" icon="el-icon-delete"></el-button>
      </el-popconfirm>

      <el-table :data="tableData"  stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="文件名称" width="300px"></el-table-column>
      <el-table-column prop="fileSize" label="文件大小"></el-table-column>
      <el-table-column label="下载">
        <template slot-scope="scope">
          <el-button icon="el-icon-download" circle @click="download(scope.row.fileUrl)"></el-button>
        </template>
      </el-table-column>

     
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[4,8]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>

export default {
  name: "File",
  data() {
    return {
      serverIp: 'http://localhost:9999',
      tableData: [],
      fileName: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 8,
      total: 0
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/file/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          fileName: this.fileName,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    del(id) {
      this.request.delete("/file/" + id).then(res => {
        if (res.code === 200) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)
      this.request.post("/file/del/batch", ids).then(res => {
        if (res.code === 200) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    handleFileUploadSuccess() {
      this.$message.success("上传成功")
      this.load()
    },
    download(url) {
      window.open(url)
    },

  }
}
</script>

<style scoped>

</style>
