import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServicePanelComponent } from './service-panel.component';

describe('ServicePanelComponent', () => {
  let component: ServicePanelComponent;
  let fixture: ComponentFixture<ServicePanelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServicePanelComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ServicePanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
