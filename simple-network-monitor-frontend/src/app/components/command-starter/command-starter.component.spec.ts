import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CommandStarterComponent } from './command-starter.component';

describe('CommandStarterComponent', () => {
  let component: CommandStarterComponent;
  let fixture: ComponentFixture<CommandStarterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CommandStarterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CommandStarterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
