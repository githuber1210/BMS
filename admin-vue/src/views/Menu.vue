<template>
  <div>
    <el-button icon="el-icon-plus"  @click="add(null)">添加菜单</el-button>
    <el-table :data="tableData"  stripe row-key="id">
      <el-table-column prop="name" label="菜单名称"></el-table-column>
      <el-table-column label="图标" width="50">
        <template slot-scope="scope">
          <span :class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="path" label="路由"></el-table-column>
      <el-table-column prop="component" label="组件名称"></el-table-column>
      <el-table-column prop="time" label="最后修改时间"></el-table-column>
      <el-table-column>
        <template slot-scope="scope">
          <el-button icon="el-icon-plus" @click="add(scope.row.id)" style="margin-left:10px"></el-button>
          <el-button  icon="el-icon-edit" @click="edit(scope.row)" ></el-button>
          <el-popconfirm
              style="margin-left:10px"
              confirm-button-text='确定'
              cancel-button-text='取消'
              icon="el-icon-info"
              icon-color="red"
              title="确定删除？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference" icon="el-icon-delete" ></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="新建/修改菜单" :visible.sync="dialogFormVisible">
      <el-form>
        <el-form-item label="菜单名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="form.path"></el-input>
        </el-form-item>
        <el-form-item label="组件">
          <el-input v-model="form.component"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-select v-model="form.icon">
            <el-option v-for="item in icons" :key="item.id" :label="item.iconName" :value="item.iconName">
              <i :class="item.iconName" />{{ item.iconName }}
            </el-option>
          </el-select>
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
export default {
  name: "Menu",
  data() {
    return {
      tableData: [],
      name: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      icons: [],
      pid:''
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/menu").then(res => {
        this.tableData = res.data
      })
      this.request.get("/menu/icons").then(res => {
        this.icons = res.data
      })
    },
    save() {
      this.request.post("/menu", this.form).then(res => {
        if (res.code === 200) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        }
      })
    },
    add(pid) {
      this.dialogFormVisible = true
      this.form = {}
      if (pid) {
        this.form.pid = pid
      }
    },
    edit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/menu/" + id).then(res => {
        if (res.code === 200) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },

  }
}
</script>


<style>

</style>
