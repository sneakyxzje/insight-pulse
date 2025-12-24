import { getMe } from "@src/lib/api/user";
import { syncAuthCookie } from "@src/lib/utils/auth";
import { redirect } from "@sveltejs/kit";

export const handle = async ({ event, resolve }) => {
  const jwt = event.cookies.get("jwt");
  const refreshToken = event.cookies.get("refresh_token");
  if (jwt) {
    try {
      const user = await getMe(event.fetch);
      event.locals.user = user;
    } catch (error: any) {
      if ((error.status === 401 || error.status === 403) && refreshToken) {
        await tryToRefresh(event);
      }
    }
  } else if (refreshToken) {
    await tryToRefresh(event);
  }
  const isAppRoute = event.route.id?.includes("(app)");

  if (isAppRoute && !event.locals.user) {
    throw redirect(303, "/login");
  }
  if (
    event.locals.user &&
    (event.url.pathname === "/login" || event.url.pathname === "/register")
  ) {
    throw redirect(303, "/campaigns");
  }
  if (event.locals.user && event.url.pathname === "/") {
    throw redirect(303, "/campaigns");
  }
  return resolve(event);
};

async function tryToRefresh(event: any) {
  try {
    const refreshRes = await event.fetch(
      "http://localhost:8080/api/auth/refresh",
      { method: "POST" }
    );

    if (refreshRes.ok) {
      syncAuthCookie(refreshRes, event);
      const user = await getMe(event.fetch);
      event.locals.user = user;
    } else {
      event.locals.user = null;
    }
  } catch (err) {
    event.locals.user = null;
  }
}
