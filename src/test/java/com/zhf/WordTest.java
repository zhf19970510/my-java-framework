package com.zhf;

import cn.hutool.core.io.FileUtil;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.*;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * https://www.bilibili.com/video/BV18X4y1v7yD/?spm_id_from=333.337.search-card.all.click&vd_source=fc4f6ec59c9fcc819db3b487d9b110f5
 */
public class WordTest {

    @Test
    public void test1() throws IOException {
        File file = ResourceUtils.getFile("classpath:doc/test.docx");
        String imgPath = "E:\\workspace\\code\\my-java-framework\\src\\main\\resources\\doc\\hz.png";
        String outPath = "E:\\workspace\\code\\my-java-framework\\src\\main\\resources\\doc\\test.docx";
        Map<String, Object> map = new HashMap<>();

        // 文本
        map.put("name", "张三");
        map.put("age", "20");
        map.put("edu", "本科");


        // 表格
        RowRenderData row0 = Rows.of("姓名", "年龄", "学历", "照片").textColor("ffffff")
                .bgColor("4472C4").center().create();
        RowRenderData row1 = Rows.of("张三", "20", "本科").center().create();
        RowRenderData row2 = Rows.of("李四", "22", "本科").center().create();

        // 单元格插入图片
        PictureRenderData pic = Pictures.ofLocal(imgPath).size(50, 50).create();
        row1.addCell(Cells.of(pic).create());
        row2.addCell(Cells.of(pic).create());

        TableRenderData tableRenderData = Tables.create(row0, row1);
        tableRenderData.addRow(row2);
        map.put("table", tableRenderData);

        // 图片  本地、流、url、bytes
        map.put("img", Pictures.ofLocal(imgPath).create());
        map.put("streamImg", Pictures.ofStream(Files.newInputStream(new File(imgPath).toPath()))
                .size(100, 100).create());
        map.put("urlImg", Pictures.ofUrl("https://img-blog.csdnimg.cn/522902589b3d4c21b30e7bc229897b41.png")
                .size(150, 150).create());
        map.put("byteImg", Pictures.ofBytes(FileUtil.readBytes(imgPath)).size(200, 200).create());
        XWPFTemplate template = XWPFTemplate.compile(file).render(map);
        template.writeAndClose(Files.newOutputStream(Paths.get(outPath)));
    }

    @Test
    public void test2() throws IOException {
        // word模板
        String path = "D:\\B站\\基础讲解\\vue3\\springboot-vue3\\src\\main\\resources\\doc\\test2.docx";
        XWPFTemplate template = XWPFTemplate.compile(path);  // 读取模板内容
        Map<String, Object> map = new HashMap<>();
        map.put("date", "2023-07-02");  // map里面的变量名称一定要跟模板里的一致
        template.render(map);
        template.writeAndClose(Files.newOutputStream(Paths.get("D:\\B站\\基础讲解\\vue3\\springboot-vue3\\src\\main\\resources\\doc\\text.docx")));
    }

    @Test
    public void test3() throws IOException {
        // word模板
        String path = "D:\\B站\\基础讲解\\vue3\\springboot-vue3\\src\\main\\resources\\doc\\tableTemp.docx";
        XWPFTemplate template = XWPFTemplate.compile(path);  // 读取模板内容
        Map<String, Object> map = new HashMap<>();

        RowRenderData row0 = Rows.of("姓名", "年龄", "头像").center().textColor("FFFFFF").bgColor("6495ED").create();
        RowRenderData row1 = Rows.of("张三", "20").center().create();
        PictureRenderData img = Pictures.of("D:\\B站\\基础讲解\\vue3\\springboot-vue3\\src\\main\\resources\\doc\\hz.png")
                .size(50, 50).create();
        CellRenderData cellRenderData = Cells.of(img).create();
        row1.addCell(cellRenderData);

        RowRenderData row2 = Rows.of("李四", "22").center().create();
        row2.addCell(cellRenderData);
        TableRenderData tableRenderData = Tables.create(row0, row1, row2);

        map.put("table", tableRenderData);  // map里面的变量名称一定要跟模板里的一致
        template.render(map);
        template.writeAndClose(Files.newOutputStream(Paths.get("D:\\B站\\基础讲解\\vue3\\springboot-vue3\\src\\main\\resources\\doc\\table.docx")));
    }

    @Test
    public void test4() throws IOException {
        // word模板
        String path = "D:\\B站\\基础讲解\\vue3\\springboot-vue3\\src\\main\\resources\\doc\\imgTemp.docx";
        XWPFTemplate template = XWPFTemplate.compile(path);  // 读取模板内容

        Map<String, Object> map = new HashMap<>();
        PictureRenderData img = Pictures.of("D:\\B站\\基础讲解\\vue3\\springboot-vue3\\src\\main\\resources\\doc\\hz.png").create();
        PictureRenderData streamImg = Pictures.ofStream(Files.newInputStream(Paths.get("D:\\B站\\基础讲解\\vue3\\springboot-vue3\\src\\main\\resources\\doc\\hz.png"))).create();
        PictureRenderData urlImg = Pictures.ofUrl("https://img-blog.csdnimg.cn/522902589b3d4c21b30e7bc229897b41.png").create();


        map.put("img", img);  // map里面的变量名称一定要跟模板里的一致
        map.put("streamImg", streamImg);
        map.put("urlImg", urlImg);
        template.render(map);
        template.writeAndClose(Files.newOutputStream(Paths.get("D:\\B站\\基础讲解\\vue3\\springboot-vue3\\src\\main\\resources\\doc\\img.docx")));
    }

}