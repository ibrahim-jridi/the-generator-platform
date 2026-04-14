import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PhysicalUserProfileComponent } from './physical-user-profile.component';

describe('PhysicalUserProfileComponent', () => {
  let component: PhysicalUserProfileComponent;
  let fixture: ComponentFixture<PhysicalUserProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PhysicalUserProfileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PhysicalUserProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
