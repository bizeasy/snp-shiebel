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

describe('Uom e2e test', () => {
  const uomPageUrl = '/uom';
  let username: string;
  let password: string;
  const uomSample = { name: 'where hopelessly' };

  let uom;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/uoms+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/uoms').as('postEntityRequest');
    cy.intercept('DELETE', '/api/uoms/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (uom) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/uoms/${uom.id}`,
      }).then(() => {
        uom = undefined;
      });
    }
  });

  it('Uoms menu should load Uoms page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('uom');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Uom').should('exist');
    cy.location('pathname').should('eq', uomPageUrl);
  });

  describe('Uom page', () => {
    it('should have translated page title', () => {
      cy.visit(uomPageUrl);
      cy.getEntityHeading('Uom').should('not.contain', 'snpApp.uom.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(uomPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Uom page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${uomPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Uom');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', uomPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/uoms',
          body: uomSample,
        }).then(({ body }) => {
          uom = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/uoms+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [uom],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(uomPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Uom page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('uom');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', uomPageUrl);
      });

      it('edit button click should load edit Uom page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Uom');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', uomPageUrl);
      });

      it('edit button click should load edit Uom page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Uom');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', uomPageUrl);
      });

      it('last delete button click should delete instance of Uom', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('uom').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', uomPageUrl);

        uom = undefined;
      });
    });
  });

  describe('new Uom page', () => {
    beforeEach(() => {
      cy.visit(uomPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Uom');
    });

    it('should create an instance of Uom', () => {
      cy.get(`[data-cy="name"]`).type('ew duh');
      cy.get(`[data-cy="name"]`).should('have.value', 'ew duh');

      cy.get(`[data-cy="description"]`).type('revere leading');
      cy.get(`[data-cy="description"]`).should('have.value', 'revere leading');

      cy.get(`[data-cy="sequenceNo"]`).type('3403');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '3403');

      cy.get(`[data-cy="abbreviation"]`).type('why howl f');
      cy.get(`[data-cy="abbreviation"]`).should('have.value', 'why howl f');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        uom = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', uomPageUrl);
    });
  });
});
