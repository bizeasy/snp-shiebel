import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { PartyClassificationTypeDetail } from './party-classification-type-detail';

describe('PartyClassificationType Management Detail Component', () => {
  let comp: PartyClassificationTypeDetail;
  let fixture: ComponentFixture<PartyClassificationTypeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./party-classification-type-detail').then(m => m.PartyClassificationTypeDetail),
              resolve: { partyClassificationType: () => of({ id: 21929 }) },
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
    fixture = TestBed.createComponent(PartyClassificationTypeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load partyClassificationType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PartyClassificationTypeDetail);

      // THEN
      expect(instance.partyClassificationType()).toEqual(expect.objectContaining({ id: 21929 }));
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
