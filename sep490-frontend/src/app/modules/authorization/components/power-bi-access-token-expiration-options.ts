import {SelectableItem} from '../../shared/models/base-models';

export class PowerBiAccessTokenExpirationOptions {
  public static defaultExpirationTimeOption: SelectableItem<Date> = {
    label: 'powerBi.accessToken.expiration.30days',
    value: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000)
  };
  public static customExpirationTimeOption: SelectableItem<null> = {
    label: 'powerBi.accessToken.expiration.custom',
    value: null
  };
  public static selectableExpirationTimes: SelectableItem<Date | null>[] = [
    {
      label: 'powerBi.accessToken.expiration.7days',
      value: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000)
    },
    this.defaultExpirationTimeOption,
    {
      label: 'powerBi.accessToken.expiration.60days',
      value: new Date(Date.now() + 60 * 24 * 60 * 60 * 1000)
    },
    {
      label: 'powerBi.accessToken.expiration.90days',
      value: new Date(Date.now() + 90 * 24 * 60 * 60 * 1000)
    },
    this.customExpirationTimeOption,
    {
      label: 'powerBi.accessToken.expiration.never',
      value: new Date(Date.now() + 100 * 365 * 24 * 60 * 60 * 1000)
    }
  ];
}
