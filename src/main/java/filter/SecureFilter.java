package filter;

import ninja.*;

//*** Фильтр проверяющий залогинен ли пользователь или нет, если нет, то его бросает на главную страницу
//*** фильтр работает на методах помеченных аннотацией @FilterWith(SecureFilter.class)
public class SecureFilter implements Filter {

    @Override
    public Result filter(FilterChain chain, Context context) {

        // if we got no cookies we break:
        if (context.getSession() == null || context.getSession().get("name") == null) {

            return Results.redirect("/");

        } else {
            return chain.next(context);
        }

    }
}
