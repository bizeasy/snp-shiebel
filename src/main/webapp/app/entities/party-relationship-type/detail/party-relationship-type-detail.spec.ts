import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { PartyRelationshipTypeDetail } from './party-relationship-type-detail';

describe('PartyRelationshipType Management Detail Component', () => {
  let comp: PartyRelationshipTypeDetail;
  let fixture: ComponentFixture<PartyRelationshipTypeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./party-relationship-type-detail').then(m => m.PartyRelationshipTypeDetail),
              resolve: { partyRelationshipType: () => of({ id: 13343 }) },
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
    fixture = TestBed.createComponent(PartyRelationshipTypeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load partyRelationshipType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PartyRelationshipTypeDetail);

      // THEN
      expect(instance.partyRelationshipType()).toEqual(expect.objectContaining({ id: 13343 }));
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
