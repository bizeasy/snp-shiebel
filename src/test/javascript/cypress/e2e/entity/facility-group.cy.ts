import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('FacilityGroup e2e test', () => {
  const facilityGroupPageUrl = '/facility-group';
  let username: string;
  let password: string;
  const facilityGroupSample = { name: 'excitedly minus' };

  let facilityGroup;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/facility-groups+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/facility-groups').as('postEntityRequest');
    cy.intercept('DELETE', '/api/facility-groups/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (facilityGroup) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/facility-groups/${facilityGroup.id}`,
      }).then(() => {
        facilityGroup = undefined;
      });
    }
  });

  it('FacilityGroups menu should load FacilityGroups page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('facility-group');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FacilityGroup').should('exist');
    cy.location('pathname').should('eq', facilityGroupPageUrl);
  });

  describe('FacilityGroup page', () => {
    it('should have translated page title', () => {
      cy.visit(facilityGroupPageUrl);
      cy.getEntityHeading('FacilityGroup').should('not.contain', 'snpApp.facilityGroup.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(facilityGroupPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FacilityGroup page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${facilityGroupPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FacilityGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityGroupPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/facility-groups',
          body: facilityGroupSample,
        }).then(({ body }) => {
          facilityGroup = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/facility-groups+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [facilityGroup],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(facilityGroupPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FacilityGroup page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('facilityGroup');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityGroupPageUrl);
      });

      it('edit button click should load edit FacilityGroup page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FacilityGroup');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityGroupPageUrl);
      });

      it('edit button click should load edit FacilityGroup page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FacilityGroup');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityGroupPageUrl);
      });

      it('last delete button click should delete instance of FacilityGroup', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('facilityGroup').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityGroupPageUrl);

        facilityGroup = undefined;
      });
    });
  });

  describe('new FacilityGroup page', () => {
    beforeEach(() => {
      cy.visit(facilityGroupPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FacilityGroup');
    });

    it('should create an instance of FacilityGroup', () => {
      cy.get(`[data-cy="name"]`).type('structure');
      cy.get(`[data-cy="name"]`).should('have.value', 'structure');

      cy.get(`[data-cy="description"]`).type('geez acidly chilly');
      cy.get(`[data-cy="description"]`).should('have.value', 'geez acidly chilly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        facilityGroup = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', facilityGroupPageUrl);
    });
  });
});
