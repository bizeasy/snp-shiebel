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

describe('SalesChannel e2e test', () => {
  const salesChannelPageUrl = '/sales-channel';
  let username: string;
  let password: string;
  const salesChannelSample = { name: 'gently behind blah' };

  let salesChannel;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/sales-channels+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sales-channels').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sales-channels/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (salesChannel) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sales-channels/${salesChannel.id}`,
      }).then(() => {
        salesChannel = undefined;
      });
    }
  });

  it('SalesChannels menu should load SalesChannels page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('sales-channel');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SalesChannel').should('exist');
    cy.location('pathname').should('eq', salesChannelPageUrl);
  });

  describe('SalesChannel page', () => {
    it('should have translated page title', () => {
      cy.visit(salesChannelPageUrl);
      cy.getEntityHeading('SalesChannel').should('not.contain', 'snpApp.salesChannel.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(salesChannelPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SalesChannel page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${salesChannelPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('SalesChannel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', salesChannelPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sales-channels',
          body: salesChannelSample,
        }).then(({ body }) => {
          salesChannel = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sales-channels+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [salesChannel],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(salesChannelPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SalesChannel page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('salesChannel');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', salesChannelPageUrl);
      });

      it('edit button click should load edit SalesChannel page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SalesChannel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', salesChannelPageUrl);
      });

      it('edit button click should load edit SalesChannel page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SalesChannel');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', salesChannelPageUrl);
      });

      it('last delete button click should delete instance of SalesChannel', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('salesChannel').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', salesChannelPageUrl);

        salesChannel = undefined;
      });
    });
  });

  describe('new SalesChannel page', () => {
    beforeEach(() => {
      cy.visit(salesChannelPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SalesChannel');
    });

    it('should create an instance of SalesChannel', () => {
      cy.get(`[data-cy="name"]`).type('lest putrid besides');
      cy.get(`[data-cy="name"]`).should('have.value', 'lest putrid besides');

      cy.get(`[data-cy="description"]`).type('pish');
      cy.get(`[data-cy="description"]`).should('have.value', 'pish');

      cy.get(`[data-cy="sequenceNo"]`).type('10253');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '10253');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        salesChannel = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', salesChannelPageUrl);
    });
  });
});
