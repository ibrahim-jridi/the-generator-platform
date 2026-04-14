export class ButtonOverlayModel {
  id: string;
  tooltipButton: string;
  classButton: string;
  classIcon: string;

  constructor(
    id: string,
    tooltipButton?: string,
    classButton?: string,
    classIcon?: string,
  ) {
    this.id = id;
    this.tooltipButton = tooltipButton || '';
    this.classButton = classButton || '';
    this.classIcon = classIcon || '';
  }
}
