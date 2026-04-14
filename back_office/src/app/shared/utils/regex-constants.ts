export class RegexConstants {
  public static readonly NUMERIC = /^[0-9\s]+$/;
  public static readonly EMAIL = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  public static readonly COMPANY_ADDRESS = /^[\u0600-\u06FFa-zA-ZÀ-ÖØ-öø-ÿ0-9 ,.()'_-]*$/;
  public static readonly CODE_POSTAL = /^\d{4}$/;
  public static readonly NUM_TEL = /^(?:\+216)?(2|5|9|4|7)[0-9]{7}$/;
  public static readonly NUM_FAX = /^(?:\+216)?(2|5|9|4|7)[0-9]{7}$/;
  public static readonly COMPANY_NAME = /^(?! )[A-Za-zÀ-ÖØ-öø-ÿ\u0600-\u06FF0-9 .,'&()_-]{1,99}[A-Za-zÀ-ÖØ-öø-ÿ\u0600-\u06FF0-9]$/;
  public static readonly GROUP_PATTERN_LABEL = '^[a-zA-Z_-]+$';
  public static readonly CIN = /^\d{8}$/;
  public static readonly PASSPORT = /^(?=(.*[a-zA-Z]){6,}).*$/;
  public static readonly ONLY_NUMERIC = /^[0-9]$/;
  public static readonly FIRST_NAME_LAST_NAME_REGEX = /^[A-Za-zÀ-ÿ\s]{1,50}$/;
  public static readonly TUNISIAN_PHONE_NUMBER = /^\+216[2579]\d{7}$/;
  public static readonly DATE_FORMAT = /^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[012])\/\d{4}$/ ; // 'dd/mm/yyy'
  public static readonly ADDRESS_LENGTH =  /^.{11,}$/ ;
  public static readonly PHONE_PREFIX = '+216';
  public static readonly LOWERCASE =  /^[a-z0-9]+$/;
  public static readonly NOTIF_PATTERN_LABEL= /^[a-zA-Z0-9\s]+$/;
  public static readonly PHONE_PREFIX_FRANCE = '+33';

  public static readonly TVA_REGEX = /^(100(\.0+)?|0|[1-9]?\d(\.\d+)?)$/;
}
