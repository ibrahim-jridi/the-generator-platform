import {AuthenticationMethod} from "./authentication-method.enum";

export enum ContentType {
  JSON = "JSON",
  XML = "XML",
  Zip = "Zip",
  OCTET_STREAM = "OCTET_STREAM",
  JAVASCRIPT = "JAVASCRIPT",
  EDIFACT = "EDIFACT",
  EDI_X12 = "EDI_X12",
  XHTML = "XHTML",
  JAVA_ARCHIVE = "JAVA_ARCHIVE",
  PDF = "PDF",
  URL_ENCODED = "URL_ENCODED",
  OGG = "OGG",
  LD_JSON = "LD_JSON",
  X_SHOCKWAVE_FLASH = "X_SHOCKWAVE_FLASH"
}

export const contentTypes = Object.keys(ContentType).map((key) => ({
  id: ContentType[key as keyof typeof ContentType],
  name: key,
}));
