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

describe('UomType e2e test', () => {
  const uomTypePageUrl = '/uom-type';
  let username: string;
  let password: string;
  const uomTypeSample = { name: 'phew' };

  let uomType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/uom-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/uom-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/uom-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (uomType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/uom-types/${uomType.id}`,
      }).then(() => {
        uomType = undefined;
      });
    }
  });

  it('UomTypes menu should load UomTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('uom-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UomType').should('exist');
    cy.location('pathname').should('eq', uomTypePageUrl);
  });

  describe('UomType page', () => {
    it('should have translated page title', () => {
      cy.visit(uomTypePageUrl);
      cy.getEntityHeading('UomType').should('not.contain', 'snpApp.uomType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(uomTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UomType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${uomTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('UomType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', uomTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/uom-types',
          body: uomTypeSample,
        }).then(({ body }) => {
          uomType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/uom-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [uomType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(uomTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UomType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('uomType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', uomTypePageUrl);
      });

      it('edit button click should load edit UomType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UomType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', uomTypePageUrl);
      });

      it('edit button click should load edit UomType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UomType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', uomTypePageUrl);
      });

      it('last delete button click should delete instance of UomType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('uomType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', uomTypePageUrl);

        uomType = undefined;
      });
    });
  });

  describe('new UomType page', () => {
    beforeEach(() => {
      cy.visit(uomTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UomType');
    });

    it('should create an instance of UomType', () => {
      cy.get(`[data-cy="name"]`).type('lightly');
      cy.get(`[data-cy="name"]`).should('have.value', 'lightly');

      cy.get(`[data-cy="description"]`).type('qua with upright');
      cy.get(`[data-cy="description"]`).should('have.value', 'qua with upright');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        uomType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', uomTypePageUrl);
    });
  });
});
