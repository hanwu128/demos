import pymysql

try:
    conn = pymysql.connect(host='10.100.157.202', port=3306, db='python_demo', user='root', passwd='root',
                           charset='utf8')
    cs1 = conn.cursor()

    # 插入
    #count = cs1.execute("insert into products(prod_name) values('tacok')")
    #prod_name=input("请输入产品名称：")
    #params=[prod_name]
    #count=cs1.execute('insert into products(prod_name) values(%s)',params)

    #修改
    #count = cs1.execute("update products set prod_name='xiaomi' where id=1")

    #删除
    #count = cs1.execute("delete from products where id=1")

    #查询
    cs1.execute("select * from products where id=3")
    count=cs1.fetchone()
    print
    count[0]
    conn.commit()
    cs1.close()
    conn.close()
except Exception as e:
    print
    e.message
