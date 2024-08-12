package huawei.cmsdemo.main.data.model

data class ServicesInfo (
    val servicesName: String,
    val version: String,
    val desc: String = "",
    val imageUrl: String,
    val navigationActionId: Int = 0
)
