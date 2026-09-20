import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ReturnHeaderTypeDetail } from './return-header-type-detail';

describe('ReturnHeaderType Management Detail Component', () => {
  let comp: ReturnHeaderTypeDetail;
  let fixture: ComponentFixture<ReturnHeaderTypeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./return-header-type-detail').then(m => m.ReturnHeaderTypeDetail),
              resolve: { returnHeaderType: () => of({ id: 14220 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReturnHeaderTypeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load returnHeaderType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ReturnHeaderTypeDetail);

      // THEN
      expect(instance.returnHeaderType()).toEqual(expect.objectContaining({ id: 14220 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vi.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
