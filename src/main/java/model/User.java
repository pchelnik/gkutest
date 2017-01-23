package model;

//*** Объект для пользователя, содержит только одно поле, имя, этот объект передаем при рендеринге страницы
//*** например - return Results.redirect("/user").render(user);
public class User {
    private String name;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
