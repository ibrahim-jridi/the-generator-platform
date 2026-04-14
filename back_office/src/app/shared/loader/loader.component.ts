import { Component, Input, OnInit, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.scss']
})
export class LoaderComponent implements OnInit {
  @Input() isLoading: boolean = false;
  showMessage: boolean = false;
  private timeout: any;

  ngOnInit() {
    this.checkLoading();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['isLoading']) {
      this.checkLoading();
    }
  }

  private checkLoading() {
    clearTimeout(this.timeout);
    this.showMessage = false;
    if (this.isLoading) {
      this.timeout = setTimeout(() => {
        this.showMessage = true;
      }, 5000);
    }
  }
}
