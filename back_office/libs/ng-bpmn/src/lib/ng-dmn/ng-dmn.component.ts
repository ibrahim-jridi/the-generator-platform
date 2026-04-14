import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges, ViewChild, ViewEncapsulation } from '@angular/core';
import DmnModeler from 'dmn-js/lib/Modeler';
import { DmnPropertiesPanelModule, DmnPropertiesProviderModule } from 'dmn-js-properties-panel';
import { ModelerComponent } from '../core/ModelerComponent';
import { Modeler } from '../core/Modeler';
import { Observable, Subscription, from, map, of, switchMap } from 'rxjs';
import { ImportEvent } from '../core/ImportEvent';
import DiagramActionsModule from '../core/modeling/DiagramActionsModule';
import DmnActionsModule from '../core/modeling/DmnActionsModule';
import { EditorActions } from '../core/modeling/EditorActions';
import { ModelerActions } from '../core/modeling/ModelerActions';
import { DiagramChangedEvent } from '../ng-bpmn/ng-bpmn.component';
export type DmnViewType = 'drd' | 'decisionTable' | 'literalExpression';
import camundaDmnModdleDescriptor from '../core/modeling/descriptors/camundaDmn.json';
import decisionPropertiesProviderModule from '../core/modeling/provider/Dmn/Decision/index';

export interface DmnView {
  element: any;
  id: string;
  name: string;
  type: DmnViewType;
}

@Component({
  selector: 'ng-dmn',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './ng-dmn.component.html',
  styleUrls: ['./ng-dmn.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class NgDmnComponent extends ModelerComponent implements Modeler, OnInit, OnDestroy, OnChanges {
  private dmnJS?: DmnModeler;

  @Input({ required: true }) url?: string;
  @Input() showProperties = false;
  @Input() hotkeys = false;

  @ViewChild('canvas', { static: true })
  private canvas?: ElementRef;

  @ViewChild('properties', { static: true })
  private properties?: ElementRef;

  @Output()
  importDone = new EventEmitter<ImportEvent>();

  @Output()
  changed = new EventEmitter<DiagramChangedEvent>();

  constructor(private http: HttpClient) {
    super();
  }

  ngOnInit(): void {
    this.dmnJS = new DmnModeler({
      container: this.canvas?.nativeElement,
      drd: {
        propertiesPanel: {
          parent: this.properties?.nativeElement
        },
        additionalModules: [
          DmnPropertiesPanelModule,
          DmnPropertiesProviderModule,
          DiagramActionsModule,
          DmnActionsModule,
          decisionPropertiesProviderModule
        ],
        moddleExtensions: {
          camunda: camundaDmnModdleDescriptor
        }
      }
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['url']) {
      this.loadUrl(changes['url'].currentValue);
    }
  }

  ngOnDestroy(): void {
    if (this.hotkeys) {
      this.unbindHotkeys();
    }
    this.dmnJS?.destroy();
  }

  getActiveView(): DmnView {
    return this.dmnJS?.getActiveView() as DmnView;
  }

  get editorActions(): EditorActions | undefined {
    return this.dmnJS?.getActiveViewer()?.get('editorActions');
  }

  private onLoad() {
    if (this.hotkeys) {
      this.bindHotkeys();
    }
  }

  loadUrl(url: string): Subscription {
    return this.http
      .get(url, { responseType: 'text' })
      .pipe(
        switchMap((xml: string) => this.importDiagram(xml)),
        map((result) => result.warnings)
      )
      .subscribe({
        next: (warnings) => {
          this.importDone.emit({
            type: 'success',
            warnings
          });
          this.onLoad();
        },
        error: (err: HttpErrorResponse) => {
          this.importDone.emit({
            type: 'error',
            error: err
          });
        }
      });
  }

  async saveXML(): Promise<string> {
    if (this.dmnJS) {
      const { xml } = await this.dmnJS.saveXML({ format: true });
      return this.transformXML(xml);
    } else {
      return Promise.reject('Modeler not initialized');
    }
  }

  async saveSVG(): Promise<string | undefined> {
    if (this.dmnJS) {
      const activeView = this.getActiveView();

      if (activeView && activeView.type === 'drd') {
        const viewer = this.dmnJS.getActiveViewer();
        const { svg } = await viewer.saveSVG();
        return svg;
      } else {
        return Promise.resolve(undefined);
      }
    } else {
      return Promise.reject('Modeler not initialized');
    }
  }

  public importDiagram(xml: string): Observable<{ warnings: Array<string> }> {
    if (this.dmnJS) {
      return from(this.dmnJS.importXML(xml)) as any;
    } else {
      return of({ warnings: [] });
    }
  }

  protected override bindHotkeys() {
    console.log('Binding DMN hotkeys');

    super.bindHotkeys({
      'ctrl+a, command+a': ModelerActions.selectElements,
      e: ModelerActions.directEditing,
      h: ModelerActions.handTool,
      l: ModelerActions.lassoTool,
      s: ModelerActions.spaceTool,
      'ctrl+=, command+=': ModelerActions.zoomIn,
      'ctrl+-, command+-': ModelerActions.zoomOut,
      'ctrl+0, command+0': ModelerActions.resetZoom,
      'ctrl+9, command+9': ModelerActions.zoomToFit,
      'ctrl+z, command+z': ModelerActions.undo,
      'ctrl+shift+z, command+shift+z': ModelerActions.redo,
      Backspace: ModelerActions.removeSelection,
      'ctrl+c, command+c': ModelerActions.copy,
      c: ModelerActions.globalConnectTool,
      'ctrl+v, command+v': ModelerActions.paste,
      'ctrl+x, command+x': ModelerActions.cut,
      'ctrl+f, command+f': ModelerActions.find
    });
  }

  toggleProperties() {
    this.showProperties = !this.showProperties;
  }

  private transformXML(xml: string): string {
    const parser = new DOMParser();
    const serializer = new XMLSerializer();
    const doc = parser.parseFromString(xml, 'application/xml');
    const decisions = doc.getElementsByTagName('decision');
    for (let i = 0; i < decisions.length; i++) {
      const decision = decisions[i];
      const historyTimeToLive = decision.getAttribute('historyTimeToLive');
      if (historyTimeToLive) {
        decision.setAttribute('camunda:historyTimeToLive', historyTimeToLive);
        decision.removeAttribute('historyTimeToLive');
      }
    }

    // Ensure the camunda namespace is added
    if (!doc.documentElement.hasAttribute('xmlns:camunda')) {
      doc.documentElement.setAttribute('xmlns:camunda', 'http://camunda.org/schema/1.0/dmn');
    }

    return serializer.serializeToString(doc);
  }
}
