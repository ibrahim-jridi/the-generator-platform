import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RneLoginComponent } from './rne-login.component';

describe('RneLoginComponent', () => {
  let component: RneLoginComponent;
  let fixture: ComponentFixture<RneLoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RneLoginComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RneLoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
