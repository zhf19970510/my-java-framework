T(N) = a * T(N / b) + O(N ^ d)

Master公式解释：
每个子递归规模一样，都是 N/b，且执行a次，其他时间复杂度是O(N^d)

时间复杂度确定：
![img.png](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240625060312.png)