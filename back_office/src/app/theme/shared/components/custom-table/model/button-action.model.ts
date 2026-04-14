export class ButtonActionModel {
  id: string;
  tooltipButton: string;
  classButton: string;
  classIcon: string;
  hiddenRule?: (row: any) => boolean;
  constructor(
      id: string,
      tooltipButton?: string,
      classButton?: string,
      classIcon?: string,
      hiddenRule?: (row: any) => boolean
  ) {
    this.id = id;
    this.tooltipButton = tooltipButton || '';
    this.classButton = classButton || '';
    this.classIcon = classIcon || '';
    this.hiddenRule = hiddenRule;
  }
}
