import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditConfigTemplateReportComponent } from './edit-config-template-report.component';

describe('EditConfigTemplateReportComponent', () => {
  let component: EditConfigTemplateReportComponent;
  let fixture: ComponentFixture<EditConfigTemplateReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditConfigTemplateReportComponent]
    });
    fixture = TestBed.createComponent(EditConfigTemplateReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
