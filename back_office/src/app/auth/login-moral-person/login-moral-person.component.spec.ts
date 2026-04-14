import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginMoralPersonComponent } from './login-moral-person.component';

describe('LoginMoralPersonComponent', () => {
  let component: LoginMoralPersonComponent;
  let fixture: ComponentFixture<LoginMoralPersonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginMoralPersonComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LoginMoralPersonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
