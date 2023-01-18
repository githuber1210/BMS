<template>
  <div>
      <el-input style="width: 200px" placeholder="搜索博客标题" v-model="title"></el-input>
      <el-button icon="el-icon-search"  @click="load"></el-button>
      <el-button icon="el-icon-plus"    @click="add"></el-button>
      <el-popconfirm
          confirm-button-text='确定'
          cancel-button-text='取消'
          icon="el-icon-info"
          icon-color="red"
          title="確定刪除？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference" icon="el-icon-delete"></el-button>
      </el-popconfirm>

    <el-date-picker
        v-model="value1"
        type="datetimerange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="yyyy-MM-dd HH:mm:ss"
    >
    </el-date-picker>
    <el-button type="primary" @click="findByTimeRange">搜索</el-button>
    <el-button icon="el-icon-refresh" @click="refresh"></el-button>

    <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection"></el-table-column>
      <el-table-column prop="title" label="博客标题"></el-table-column>
      <el-table-column prop="author" label="博客作者"></el-table-column>
      <el-table-column prop="createTime" label="博客上传时间"></el-table-column>
      <el-table-column>
        <template slot-scope="scope">
          <el-button @click="$router.push('/BlogDetail?id='+scope.row.id)" icon="el-icon-search"></el-button>
          <el-button @click="edit(scope.row)" icon="el-icon-edit"></el-button>
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

    <el-dialog :visible.sync="dialogFormVisible">
      <el-form >
        <el-form-item label="文章标题">
          <el-input v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item>
          <mavon-editor v-model="form.content"  ref="md"  :ishljs="true" @imgAdd="imgAdd"/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>

import axios from "axios";

export default {
  name: "Blog",
  data() {
    return {
      form: {},
      tableData: [],
      title: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 8,
      total: 0,
      dialogFormVisible: false,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      content: '',
      viewDialogVis: false,
      value1: ''
    }
  },
  created() {
    this.load()
  },
  methods: {
    // 绑定@imgAdd event
    imgAdd(pos, $file) {
      let $vm = this.$refs.md
      // 第一步.将图片上传到服务器.
      const formData = new FormData();
      formData.append('file', $file);
      axios({
        url: 'http://localhost:9999/file/upload',
        method: 'post',
        data: formData,
        headers: {'Content-Type': 'multipart/form-data'},
      }).then((res) => {
        // 第二步.将返回的url替换到文本原位置![...](./0) -> ![...](url)
        $vm.$img2Url(pos, res.data);
      })
    },
    load() {
      this.request.get("/blog/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          title: this.title,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    changeEnable(row) {
      this.request.post("/blog/update", row).then(res => {
        if (res.code === 200) {
          this.$message.success("操作成功")
        }
      })
    },
    add() {
      this.dialogFormVisible = true
      this.form = {}
    },
    edit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/blog/" + id).then(res => {
        if (res.code === 200) {
          this.$message.success("删除成功")
          this.load()
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  
      this.request.post("/blog/del/batch", ids).then(res => {
        if (res.code === 200) {
          this.$message.success("批量删除成功")
          this.load()
        }
      })
    },
    save() {
      this.request.post("/blog", this.form).then(res => {
        if (res.code === 200) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
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
    download(url) {
      window.open(url)
    },
    findByTimeRange(){
    if (!this.value1){
      this.$message.error("请选择时间")
    }else {
      this.request.get("/blog",{
        params:{
          start:this.value1.at(0),
          end:this.value1.at(1)
        }
      }).then(res=>{
        if (res.code === 200){
          this.tableData = res.data
        }
      })
    }},
    refresh(){
      this.title=''
      this.value1=''
      this.load()
    }

  }
}
</script>

<style scoped>

</style>
