package com.example.elastic;

import com.example.elastic.bean.Artitle;
import com.example.elastic.bean.Book;
import com.example.elastic.repository.BookRepository;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot03ElasticsearchApplicationTests {

    @Autowired
    JestClient jestClient;

    @Autowired
    BookRepository bookRepository;

    @Test
    public void test02() {
//        Book book = new Book();
//        book.setId(1);
//        book.setBookName("西游记");
//        book.setAuthor("吴承恩");
//        bookRepository.index(book);

        List<Book> list = bookRepository.findByBookNameLike("游");
        System.out.println(list);
    }

    @Test
    public void contextLoads() throws IOException {

        //1、给ES中索引保存一个文档
        Artitle artitle = new Artitle();
        artitle.setId(1);
        artitle.setAuthor("好消息");
        artitle.setTitle("张三");
        artitle.setContent("hello zhangsan");

        //构建一个索引功能
        Index index = new Index.Builder(artitle).index("atguigu").type("news").build();

        jestClient.execute(index);
    }

    @Test
    public void search() throws IOException {

        String json = "{\n" +
                "    \"query\" : {\n" +
                "        \"match\" : {\n" +
                "            \"content\" : \"hello\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        //构建搜多
        Search search = new Search.Builder(json).addIndex("atguigu").addType("news").build();
        SearchResult result = jestClient.execute(search);
        System.out.println(result.getJsonString());
    }

}
