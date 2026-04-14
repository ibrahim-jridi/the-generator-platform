import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigReportElementComponent } from './config-report-element.component';

describe('ConfigReportElementComponent', () => {
  let component: ConfigReportElementComponent;
  let fixture: ComponentFixture<ConfigReportElementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfigReportElementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConfigReportElementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
