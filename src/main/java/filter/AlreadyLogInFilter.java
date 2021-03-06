package filter;

import ninja.*;

//*** Фильтр проверяющий залогинен ли пользователь или нет, если да, то его бросает на страницу профиля
//*** фильтр работает на методах помеченных аннотацией @FilterWith(AlreadyLogInFilter.class)
public class AlreadyLogInFilter implements Filter {

    @Override
    public Result filter(FilterChain chain, Context context) {

        // if we got no cookies we break:
        if (context.getSession() != null && context.getSession().get("name") != null) {

            return Results.redirect("/user");

        } else {
            return chain.next(context);
        }

    }
}
