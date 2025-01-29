import {environment} from '../environments/environment';

export class AppRoutingConstants {
  public static readonly IDP_API_URL = environment.idpApiUrl;

  // To be remove
  public static readonly DEV_PATH = 'dev';

  public static readonly AUTH_PATH = 'authorization';
  public static readonly USERS_PATH = 'users';
  public static readonly USER_DETAILS = 'create-user';

  // Enterprise Module
  public static readonly ENTERPRISE_PATH = 'enterprise';
  public static readonly BUILDING_PATH = 'buildings';
  public static readonly PLAN_PATH = 'plan';
  public static readonly PAYMENT_PATH = 'payments';
  public static readonly UPDATE_USER_PATH = 'update-user';

  // Emissions Module
  public static readonly EMISSIONS_PATH = 'emissions';

  // App Routing
  public static readonly DASHBOARD_PATH = 'dashboard';
  public static readonly FORBIDDEN = 'forbidden';
  public static readonly UNAUTHORIZED = 'unauthorized';
}
