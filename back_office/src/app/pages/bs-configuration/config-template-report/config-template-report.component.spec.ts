import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigTemplateReportComponent } from './config-template-report.component';

describe('ConfigTemplateReportComponent', () => {
  let component: ConfigTemplateReportComponent;
  let fixture: ComponentFixture<ConfigTemplateReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfigTemplateReportComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConfigTemplateReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
