import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPartyClassificationType } from 'app/entities/party-classification-type/party-classification-type.model';
import { PartyClassificationTypeService } from 'app/entities/party-classification-type/service/party-classification-type.service';
import { IPartyClassificationGroup } from '../party-classification-group.model';
import { PartyClassificationGroupService } from '../service/party-classification-group.service';

import { PartyClassificationGroupFormService } from './party-classification-group-form.service';
import { PartyClassificationGroupUpdate } from './party-classification-group-update';

describe('PartyClassificationGroup Management Update Component', () => {
  let comp: PartyClassificationGroupUpdate;
  let fixture: ComponentFixture<PartyClassificationGroupUpdate>;
  let activatedRoute: ActivatedRoute;
  let partyClassificationGroupFormService: PartyClassificationGroupFormService;
  let partyClassificationGroupService: PartyClassificationGroupService;
  let partyClassificationTypeService: PartyClassificationTypeService;

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

    fixture = TestBed.createComponent(PartyClassificationGroupUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partyClassificationGroupFormService = TestBed.inject(PartyClassificationGroupFormService);
    partyClassificationGroupService = TestBed.inject(PartyClassificationGroupService);
    partyClassificationTypeService = TestBed.inject(PartyClassificationTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call PartyClassificationType query and add missing value', () => {
      const partyClassificationGroup: IPartyClassificationGroup = { id: 28249 };
      const classificationType: IPartyClassificationType = { id: 21929 };
      partyClassificationGroup.classificationType = classificationType;

      const partyClassificationTypeCollection: IPartyClassificationType[] = [{ id: 21929 }];
      vi.spyOn(partyClassificationTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: partyClassificationTypeCollection })));
      const additionalPartyClassificationTypes = [classificationType];
      const expectedCollection: IPartyClassificationType[] = [...additionalPartyClassificationTypes, ...partyClassificationTypeCollection];
      vi.spyOn(partyClassificationTypeService, 'addPartyClassificationTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partyClassificationGroup });
      comp.ngOnInit();

      expect(partyClassificationTypeService.query).toHaveBeenCalled();
      expect(partyClassificationTypeService.addPartyClassificationTypeToCollectionIfMissing).toHaveBeenCalledWith(
        partyClassificationTypeCollection,
        ...additionalPartyClassificationTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partyClassificationTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const partyClassificationGroup: IPartyClassificationGroup = { id: 28249 };
      const classificationType: IPartyClassificationType = { id: 21929 };
      partyClassificationGroup.classificationType = classificationType;

      activatedRoute.data = of({ partyClassificationGroup });
      comp.ngOnInit();

      expect(comp.partyClassificationTypesSharedCollection()).toContainEqual(classificationType);
      expect(comp.partyClassificationGroup).toEqual(partyClassificationGroup);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyClassificationGroup>();
      const partyClassificationGroup = { id: 8459 };
      vi.spyOn(partyClassificationGroupFormService, 'getPartyClassificationGroup').mockReturnValue(partyClassificationGroup);
      vi.spyOn(partyClassificationGroupService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyClassificationGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyClassificationGroup);
      saveSubject.complete();

      // THEN
      expect(partyClassificationGroupFormService.getPartyClassificationGroup).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partyClassificationGroupService.update).toHaveBeenCalledWith(expect.objectContaining(partyClassificationGroup));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyClassificationGroup>();
      const partyClassificationGroup = { id: 8459 };
      vi.spyOn(partyClassificationGroupFormService, 'getPartyClassificationGroup').mockReturnValue({ id: null });
      vi.spyOn(partyClassificationGroupService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyClassificationGroup: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyClassificationGroup);
      saveSubject.complete();

      // THEN
      expect(partyClassificationGroupFormService.getPartyClassificationGroup).toHaveBeenCalled();
      expect(partyClassificationGroupService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyClassificationGroup>();
      const partyClassificationGroup = { id: 8459 };
      vi.spyOn(partyClassificationGroupService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyClassificationGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partyClassificationGroupService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePartyClassificationType', () => {
      it('should forward to partyClassificationTypeService', () => {
        const entity = { id: 21929 };
        const entity2 = { id: 12767 };
        vi.spyOn(partyClassificationTypeService, 'comparePartyClassificationType');
        comp.comparePartyClassificationType(entity, entity2);
        expect(partyClassificationTypeService.comparePartyClassificationType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
