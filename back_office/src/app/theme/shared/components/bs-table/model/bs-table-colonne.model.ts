import {SelectOptionModel} from "./select-option.model";

export class BsTableColonneModel {

  colonneName: string;
  colonneNameId: string;
  activeSearch: boolean;
  styleColonne: string;
  isHtlmCode: boolean;
  htmlcode: string;
  selectOption: SelectOptionModel;
  isDynamicHtmlCode: boolean;

  constructor(colonneName: string,
              colonneNameId: string,
              aciveSearch?: boolean,
              styleColonne?: string,
              isHtlmCode?: boolean,
              htmlCode?: string,
              selectOption?: SelectOptionModel,
              isDynamicHtmlCode?: boolean) {
    this.colonneName = colonneName || '';
    this.colonneNameId = colonneNameId || '';
    this.activeSearch = aciveSearch || false;
    this.styleColonne = styleColonne || null;
    this.isHtlmCode = isHtlmCode || false;
    this.htmlcode = htmlCode || '';
    this.selectOption = selectOption || null;
    this.isDynamicHtmlCode = isDynamicHtmlCode || false;
  }
}
