import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgencyPctComponent } from './agency-pct.component';

describe('AgencyPctComponent', () => {
  let component: AgencyPctComponent;
  let fixture: ComponentFixture<AgencyPctComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgencyPctComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AgencyPctComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
