// Never import this file directly. Use the environment.ts file in the same directory
export const environment = {
  production: false,
  oidcAuthority: 'http://localhost:8180/idp',
  accountPageIDP: 'http://localhost:8180/idp/account',
  idpApiUrl: 'http://localhost:8180/idp/api',
  enterpriseUrl: 'http://localhost:8080/enterprise',
  oidcClientId: 'oidc-client'
};
