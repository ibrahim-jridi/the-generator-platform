import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListConfigTemplateReportComponent } from './list-config-template-report.component';

describe('ListConfigTemplateReportComponent', () => {
  let component: ListConfigTemplateReportComponent;
  let fixture: ComponentFixture<ListConfigTemplateReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListConfigTemplateReportComponent]
    });
    fixture = TestBed.createComponent(ListConfigTemplateReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
