import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MembreAssigneeComponent } from './membre-assignee.component';

describe('MembreAssigneeComponent', () => {
  let component: MembreAssigneeComponent;
  let fixture: ComponentFixture<MembreAssigneeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MembreAssigneeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MembreAssigneeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
