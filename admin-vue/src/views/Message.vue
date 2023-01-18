<template>
  <div>
    <div style="display: flex;" v-for="item in messages">
      <div style="flex: 1">
        <el-image :src="item.avatar"></el-image>
      </div>
      <div style="flex: 9">
        <div>
          <b>{{ item.username }}:</b>
        </div>
        <div style="padding: 10px 0;">
          {{ item.content }}<el-button type="text" @click="del(item.id)">删除</el-button>
        </div>
        <div style="border:#3F5EFB solid 1px;padding: 10px" v-if="item.parentMessage">
          @{{ item.username }}：{{ item.parentMessage.content }}
        </div>
        <div style="font-size: 12px">
          <span>{{ item.time  }}</span>
          <el-button type="text" @click="reReply(item.id)">回复</el-button>
        </div>
      </div>
    </div>

    <div>
      <el-button type="primary" @click="save">留言</el-button>
      <el-input type="text" v-model="form.content"></el-input>
    </div>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5,10,20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next"
        :total="total">
    </el-pagination>

    <el-dialog title="回复信息" :visible.sync="dialogFormVisible">
      <el-form :model="form">
        <el-form-item label="内容">
          <el-input v-model="form.reply"></el-input>
        </el-form-item>
      </el-form>
      <template slot="footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="reply">确 定</el-button>
      </template>
    </el-dialog>

  </div>

</template>

<script>
import request from "@/util/request";

export default {
  name: "Message",
  data() {
    return {
      user: {},
      messages: [],
      dialogFormVisible:false,
      isCollapse: false,
      form: {}
    }
  },
  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {};
    this.loadMessage();
  },
  methods: {
    loadMessage() {
      request.get("/message/foreign/0").then(res => {
        this.messages = res.data;
      })
    },
    reReply(id) {
      console.log("reply")
      this.form={}
      this.dialogFormVisible = true;
      this.form.parentId = id;
    },
    reply() {
      this.form.content = this.form.reply;
      this.save();
    },
    save() {
      if (!this.form.content) {
        this.$message.warning("内容不能为空！")
        return;
      }
      // 如果是评论的话，在 save的时候要注意设置 当前模块的id为 foreignId。也就是  form.foreignId = 模块id
      request.post("/message", this.form).then(res => {
        if (res.code === 200) {
         this.$message.success("留言成功！")
        }
        this.form = {}
        this.loadMessage();
        this.dialogFormVisible = false;
      })
    },
    del(id) {
      request.delete("/message/" + id).then(res => {
        this.$message.success("删除成功！")
        this.loadMessage()
      })
    }
  }
}
</script>
