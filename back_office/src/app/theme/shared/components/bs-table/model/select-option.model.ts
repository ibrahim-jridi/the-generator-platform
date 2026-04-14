export class SelectOptionModel {
  isMultipleSelect: boolean;
  options: any[];
  disableSelectAttribute: string;

  constructor(isMultipleSelect?: boolean,
              options?: any[],
              disableSelectAttribute?: string) {
    this.isMultipleSelect = isMultipleSelect || false;
    this.options = options || [];
    this.disableSelectAttribute = disableSelectAttribute || null;
  }
}
