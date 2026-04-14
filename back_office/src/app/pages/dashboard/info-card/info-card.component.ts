import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ThemeService } from '../../../shared/services/theme.service';

@Component({
  selector: 'app-info-card',
  templateUrl: './info-card.component.html',
  styleUrls: ['./info-card.component.scss']
})
export class InfoCardComponent {
  @Input() cardData: any;
  @Input() isSelected: boolean = false;
  @Output() cardClick = new EventEmitter<void>();
  private greenColor: string;
  private blueColor: string;
  private orangeColor: string;

  constructor(private themeService: ThemeService) {}

  ngOnInit(): void {
    this.greenColor = this.themeService.getCSSVariable('--medium-green');
    this.blueColor = this.themeService.getCSSVariable('--cool-blue-color');
    this.orangeColor = this.themeService.getCSSVariable('--orange-color');
    console.log("cardData",this.cardData)
  }
  public onCardClick() {
    this.cardClick.emit();
  }

  public getColorForStat(index: number, totalStats: number): string {
    const colors = [this.greenColor, this.blueColor, this.orangeColor];
    return colors[index];
  }
}
