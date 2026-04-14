import { Component, Input, ViewEncapsulation, inject } from '@angular/core';
import { Modeler, ModelingService, ModelerActions } from 'libs/ng-bpmn/src';


@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss'],
})
export class AppToolbarComponent {
  private modelingService = inject(ModelingService);

  @Input({ required: true }) modeler?: Modeler;

  ModelerActions = ModelerActions;

  async saveXML() {
    if (this.modeler) {
      this.modelingService.downloadXML(this.modeler);
    }
  }

  async saveSVG() {
    if (this.modeler) {
      this.modelingService.downloadSVG(this.modeler);
    }
  }

  toggleProperties(): void {
    this.modeler?.toggleProperties();
  }
}
