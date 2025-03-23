import {environment} from '../environments/environment';

export class AppRoutingConstants {
  public static readonly IDP_API_URL = environment.idpApiUrl;
  public static readonly ENTERPRISE_API_URL = environment.enterpriseUrl;

  // To be remove
  public static readonly DEV_PATH = 'dev';

  public static readonly AUTH_PATH = 'authorization';
  public static readonly USERS_PATH = 'users';
  public static readonly ADMIN_PATH = 'admin';
  public static readonly USER_DETAILS = 'user-details';

  // Enterprise Module
  public static readonly ENTERPRISE_PATH = 'enterprise';
  public static readonly BUILDING_PATH = 'buildings';
  public static readonly EMISSION_ACTIVITY_PATH = 'emission-activity';
  public static readonly EMISSION_ACTIVITY_DETAIL_PATH = 'emission-activity-detail';
  public static readonly PLAN_PATH = 'plan';
  public static readonly PAYMENT_PATH = 'payment';

  // Emissions Module
  public static readonly EMISSIONS_PATH = 'emissions';

  // Admin Module
  public static readonly PACKAGE_CREDIT_PATH = 'package-credit';
  public static readonly PACKAGE_CREDIT_DETAILS_PATH = 'package-credit-details';
  public static readonly CREDIT_CONVERT_RATIO = 'credit-convert-ratio';
  public static readonly CREDIT_CONVERT_RATIO_DETAILS =
    'credit-convert-ratio-detail';
  public static readonly EMISSION_SOURCE = 'emission-resource';
  public static readonly FUEL_CONVERSION = 'fuel-conversion';

  // App Routing
  public static readonly LANDING_PAGE_PATH = 'landing-page';
  public static readonly DASHBOARD_PATH = 'dashboard';
  public static readonly FORBIDDEN = 'forbidden';
  public static readonly UNAUTHORIZED = 'unauthorized';

  // Third party
  public static readonly GEOCODING_PATH = 'geocoding';
}
