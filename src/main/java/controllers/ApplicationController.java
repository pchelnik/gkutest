/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import com.google.inject.Inject;
import filter.AlreadyLogInFilter;
import filter.SecureFilter;
import model.User;

import com.google.inject.Singleton;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.i18n.Lang;
import ninja.params.Param;
import ninja.session.Session;
import ninja.uploads.DiskFileItemProvider;
import ninja.uploads.FileProvider;
import org.apache.commons.io.IOUtils;

import java.io.*;


@Singleton
public class ApplicationController {

    @Inject
    Lang lang;

    //*** Метод генерит главную страницу, где нужно ввести логин
    @FilterWith(AlreadyLogInFilter.class)
    public Result index(@Param("language") String language, Session session) {
        return Results.html();
    }

    //*** Метод для отображения залогиненного пользователя
    @FilterWith(SecureFilter.class)
    public Result user(Session session) {
        String username = session.get("name");
        User user = new User(username);
        return Results.ok().render(user);

    }

    //*** Метод сохраняющий имя пользователя в куках и изображение
    @FileProvider(DiskFileItemProvider.class)
    public Result save(@Param("name") String name, @Param("upfile") InputStream upfile, Context context) throws IOException {
        if (upfile != null) {
            File file = new File("src/main/java/assets/img/avatar.jpg");
            try (FileOutputStream fop = new FileOutputStream(file)) {
                // if file doesn't exists, then create it
                if (!file.exists()) {
                    file.createNewFile();
                }
                byte[] contentInBytes = IOUtils.toByteArray(upfile);
                fop.write(contentInBytes);
                fop.flush();
                fop.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        context.getSession().put("name", name);
        User user = new User(name);
        return Results.redirect("/user").render(user);

    }

    //*** Метод удаляющий имя пользователя из кукисов
    public Result exit(Context context) {
        context.getSession().remove("name");

        return Results.redirect("/");

    }

    //*** Метод меняющий язык интерфейса (значения для разных языков в файлах messages.properties и messages_ru.properties)
    public Result language(@Param("language") String language, Context context) {
        return lang.setLanguage(language, Results.redirect("/user"));
    }

    //*** Метод меняющий стиль на темный или светлый, если стиль темный то в куках есть параметр theme, если светлый, то параметра нет
    public Result theme(@Param("theme") String theme, Context context) {
        if (theme.equals("dark")) {
            context.getSession().put("theme", theme);
        } else {
            context.getSession().remove("theme");
        }
        return Results.redirect("/user");
    }
}
