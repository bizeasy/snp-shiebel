import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPartyType } from '../party-type.model';
import { PartyTypeService } from '../service/party-type.service';

import { PartyTypeFormService } from './party-type-form.service';
import { PartyTypeUpdate } from './party-type-update';

describe('PartyType Management Update Component', () => {
  let comp: PartyTypeUpdate;
  let fixture: ComponentFixture<PartyTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let partyTypeFormService: PartyTypeFormService;
  let partyTypeService: PartyTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(PartyTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partyTypeFormService = TestBed.inject(PartyTypeFormService);
    partyTypeService = TestBed.inject(PartyTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const partyType: IPartyType = { id: 27525 };

      activatedRoute.data = of({ partyType });
      comp.ngOnInit();

      expect(comp.partyType).toEqual(partyType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyType>();
      const partyType = { id: 3927 };
      vi.spyOn(partyTypeFormService, 'getPartyType').mockReturnValue(partyType);
      vi.spyOn(partyTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyType);
      saveSubject.complete();

      // THEN
      expect(partyTypeFormService.getPartyType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partyTypeService.update).toHaveBeenCalledWith(expect.objectContaining(partyType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyType>();
      const partyType = { id: 3927 };
      vi.spyOn(partyTypeFormService, 'getPartyType').mockReturnValue({ id: null });
      vi.spyOn(partyTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyType);
      saveSubject.complete();

      // THEN
      expect(partyTypeFormService.getPartyType).toHaveBeenCalled();
      expect(partyTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyType>();
      const partyType = { id: 3927 };
      vi.spyOn(partyTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partyTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
