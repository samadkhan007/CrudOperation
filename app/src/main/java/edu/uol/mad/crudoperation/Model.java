package edu.uol.mad.crudoperation;

public class Model {

    String id,title,desc;

    public Model() {}

    public Model(String id,String title, String desc)
    {
        this.id=id;
        this.title=title;
        this.desc=desc;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }
}
